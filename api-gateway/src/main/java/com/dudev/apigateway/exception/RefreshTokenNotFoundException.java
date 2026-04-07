package com.dudev.apigateway.exception;

/**
 * Исключение, бросаемое, когда запрашиваемый Refresh‑токен не найден.
 *
 * @since 1.0.0
 */

public class RefreshTokenNotFoundException extends RuntimeException{

    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
