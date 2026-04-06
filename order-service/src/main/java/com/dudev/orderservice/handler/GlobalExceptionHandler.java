package com.dudev.orderservice.handler;

import com.dudev.orderservice.dto.ErrorResponse;
import com.dudev.orderservice.exception.NotEnoughProductsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для REST‑слоя.
 *
 * <p>Перехватывает {@link NotEnoughProductsException} и любые другие
 * исключения, преобразуя их в {@link ErrorResponse} с соответствующим HTTP‑статусом.
 *
 * @see NotEnoughProductsException
 * @see ErrorResponse
 * @author ussdanil
 * @since 1.0.0
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotEnoughProductsException.class)
    public ResponseEntity<ErrorResponse> notEnoughProductsHandler(NotEnoughProductsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage(),
                HttpStatus.CONFLICT.name(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> allHandler(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now()));
    }
}