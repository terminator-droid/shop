package com.dudev.apigateway.exception;

import java.util.UUID;

/**
 * Исключение, информирующее о том, что пользователь не найден.
 *
 * @since 1.0.0
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User not found with id = " + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found with username = " + username);
    }
}
