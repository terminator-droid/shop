package com.dudev.inventoryservice.context;

import java.util.UUID;

/**
 * Запись, представляющая данные пользователя, связанные с текущим запросом.
 *
 * @param userId   уникальный идентификатор пользователя
 * @param username имя пользователя
 * @since 1.0.0
 */

public record UserInfo(UUID userId, String username) {
}
