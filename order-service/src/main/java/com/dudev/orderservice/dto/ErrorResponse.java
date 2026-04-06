package com.dudev.orderservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO, представляющий ошибку, возвращаемую клиенту API.
 *
 * <p>Содержит человекочитаемое сообщение, машинный код ошибки
 * и метку времени формирования ответа.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Data
@RequiredArgsConstructor
public class ErrorResponse {

    private final String msg;
    private final String code;
    private final LocalDateTime timestamp;
}
