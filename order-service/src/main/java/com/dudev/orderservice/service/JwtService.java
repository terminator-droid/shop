package com.dudev.orderservice.service;

import java.util.UUID;

/**
 * Сервис, отвечающий за извлечение данных из JWT‑токена.
 *
 * <p>Оставлен как интерфейс – конкретная реализация может использовать
 * любую JWT‑библиотеку (jjwt, nimbus, etc.).
 *
 * @author ussdanil
 * @since 1.0.0
 */

public interface JwtService {

    String extractUserName(String token);

    UUID extractUserId(String token);

}
