package com.dudev.apigateway.exception;

/**
 * Исключение, бросаемое, когда Refresh‑токен просрочен.
 *
 * @since 1.0.0
 */

public class RefreshTokenExpiredException extends RuntimeException {

    public RefreshTokenExpiredException() {
        super("Refresh token is expired, please log in");
    }

}
