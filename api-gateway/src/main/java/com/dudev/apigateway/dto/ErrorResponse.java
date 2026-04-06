package com.dudev.apigateway.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO, представляющий ошибку, возвращаемую клиенту API‑gateway.
 *
 * <p>Содержит человекочитаемое сообщение, код ошибки и временную метку
 * формирования ответа.
 *
 * @since 1.0.0
 */

@Data
@RequiredArgsConstructor
public class ErrorResponse {

    private final String msg;
    private final String code;
    private final LocalDateTime timestamp;
}
