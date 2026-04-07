package com.dudev.inventoryservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO, описывающий ошибку, возвращаемую клиенту API.
 *
 * <p>Содержит человекочитаемое сообщение, машинный код ошибки и
 * временную метку формирования ответа.
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
