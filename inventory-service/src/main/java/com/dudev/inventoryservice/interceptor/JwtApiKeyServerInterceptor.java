package com.dudev.inventoryservice.interceptor;

import com.dudev.inventoryservice.context.UserContextHolder;
import com.dudev.inventoryservice.service.JwtService;
import com.dudev.inventoryservice.util.ApiKeyProperties;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * gRPC‑interceptor, реализующий аутентификацию по JWT и API‑key.
 *
 * <p>Для защищённых методов проверяется наличие корректного JWT
 * (извлекаются имя пользователя и UUID) либо валидного API‑key.
 * При успехе данные пользователя помещаются в {@link com.dudev.inventoryservice.context.UserContextHolder}.
 *
 * @see com.dudev.inventoryservice.service.JwtService
 * @see com.dudev.inventoryservice.util.ApiKeyProperties
 * @since 1.0.0
 */

@GrpcGlobalServerInterceptor
@Component
@RequiredArgsConstructor
public class JwtApiKeyServerInterceptor implements ServerInterceptor {

    private final JwtService jwtService;
    private final ApiKeyProperties apiKeyProperties;

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String API_KEY_HEADER_NAME = "X-API-Key";
    private static final String API_PREFIX = "/api";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        try {
            String authHeader = headers.get(Metadata.Key.of(AUTHORIZATION_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER));
            String apiKeyHeader = headers.get(Metadata.Key.of(API_KEY_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER));

            String method = call.getMethodDescriptor().getFullMethodName();

            if (isPublicMethod(method)) {
                return next.startCall(call, headers);
            }

            // проверка JWT
            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                String jwt = authHeader.substring(BEARER_PREFIX.length());
                String username = jwtService.extractUserName(jwt);
                UUID userId = jwtService.extractUserId(jwt);
                UserContextHolder.setUser(userId, username);
            }
            // проверка API‑Key
            else if (apiKeyHeader != null && apiKeyProperties.getApiKeys().contains(apiKeyHeader)) {
                // всё ок, пропускаем
            } else {
                throw Status.UNAUTHENTICATED
                        .withDescription("Invalid or missing JWT or API key")
                        .asRuntimeException();
            }

            return next.startCall(call, headers);

        } catch (Exception e) {
            Status status = Status.UNAUTHENTICATED
                    .withDescription("Authentication failed: " + e.getMessage());
            call.close(status, headers);
            return new ServerCall.Listener<>() {
            };
        }
    }

    private boolean isPublicMethod(String method) {
        return !method.startsWith(API_PREFIX);
    }
}