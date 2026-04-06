package com.dudev.apigateway.handler;


import com.dudev.apigateway.dto.ErrorResponse;
import com.dudev.apigateway.exception.RefreshTokenExpiredException;
import com.dudev.apigateway.exception.RefreshTokenNotFoundException;
import com.dudev.apigateway.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class, RefreshTokenNotFoundException.class})
    public ResponseEntity<ErrorResponse> notFoundHandler(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage(),
                HttpStatus.NOT_FOUND.name(), LocalDateTime.now()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> tokenExpiredHandler(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(exception.getMessage(),
                HttpStatus.UNAUTHORIZED.name(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> allHandler(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(), LocalDateTime.now()));
    }
}