package com.dudev.inventoryservice.interceptor;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * gRPC‑interceptor, копирующий trace‑id из заголовков в MDC
 * для последующего логирования запросов.
 *
 * <p>Позволяет сквозную трассировку между сервисами через поле
 * {@code X-Trace-Id}.
 *
 * @since 1.0.0
 */

@GrpcGlobalServerInterceptor
@Component
public class GrpcServerInterceptor implements ServerInterceptor {

    private static final String TRACE_ID = "X-Trace-Id";
    public static final String API_KEY_HEADER_NAME = "X-API-Key";
    public static final String USER_ID = "User-Id";
    public static final String USER_NAME = "User-Name";

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        Metadata.Key<String> key = Metadata.Key.of(TRACE_ID, Metadata.ASCII_STRING_MARSHALLER);
        String traceId = metadata.get(key);
        if (traceId != null) {
            MDC.put(TRACE_ID, traceId);
        }
        Context.Key<Object> contextKey = Context.key(TRACE_ID);

        return Contexts.interceptCall(Context.current().withValue(contextKey, traceId), serverCall, metadata, serverCallHandler);
    }
}