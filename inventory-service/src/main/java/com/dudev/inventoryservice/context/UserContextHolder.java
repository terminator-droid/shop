package com.dudev.inventoryservice.context;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Хранилище контекста текущего пользователя в {@link ThreadLocal}.
 *
 * <p>Позволяет получать {@code userId} и {@code username} в любой точке
 * выполнения текущего запроса без передачи параметров.
 *
 * @since 1.0.0
 */

@Component
public class UserContextHolder {

    private static final ThreadLocal<UserInfo> USER_CONTEXT = new ThreadLocal<>();

    public static void setUser(UUID userId, String username) {
        USER_CONTEXT.set(new UserInfo(userId, username));
    }

    public static UUID getUserId() {
        return USER_CONTEXT.get().userId();
    }

    public static String getUsername() {
        return USER_CONTEXT.get().username();
    }
}
