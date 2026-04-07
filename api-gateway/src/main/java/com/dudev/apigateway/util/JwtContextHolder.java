package com.dudev.apigateway.util;

import org.springframework.stereotype.Component;

@Component
public class JwtContextHolder {

    private static final ThreadLocal<String> JWT_HOLDER = new ThreadLocal<>();

    public static void setJwt(String jwt) {
        JWT_HOLDER.set(jwt);
    }

    public static String getJwt() {
        return JWT_HOLDER.get();
    }
}
