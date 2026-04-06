package com.dudev.orderservice.interceptor;

import com.dudev.orderservice.context.UserContextHolder;
import com.dudev.orderservice.util.ApiKeyProperties;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * gRPC‑интерцептор, добавляющий в каждый запрос:
 *
 * <ul>
 *   <li>trace‑id (из MDC)</li>
 *   <li>API‑key для Order‑service</li>
 *   <li>идентификатор и имя пользователя (из {@link UserContextHolder})</li>
 * </ul>
 *
 * Это обеспечивает сквозную трассировку и авторизацию между микросервисами.
 *
 * @see UserContextHolder
 * @see ApiKeyProperties
 * @since 1.0.0
 */

@Component
@RequiredArgsConstructor
public class GrpcClientInterceptor implements ClientInterceptor {

    private final ApiKeyProperties apiKeyProperties;

    private static final String TRACE_ID = "X-Trace-Id";
    public static final String API_KEY_HEADER_NAME = "X-API-Key";
    public static final String USER_ID = "User-Id";
    public static final String USER_NAME = "User-Name";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String traceId = MDC.get(TRACE_ID);
                if (traceId != null) {
                    headers.put(Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER), traceId);
                }
                headers.put(Metadata.Key.of(API_KEY_HEADER_NAME, Metadata.ASCII_STRING_MARSHALLER), apiKeyProperties.getOrderService());
                headers.put(Metadata.Key.of(USER_ID, Metadata.ASCII_STRING_MARSHALLER), UserContextHolder.getUserId().toString());
                headers.put(Metadata.Key.of(USER_NAME, Metadata.ASCII_STRING_MARSHALLER), UserContextHolder.getUsername());
                super.start(responseListener, headers);
            }
        };
    }
}