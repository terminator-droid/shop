package com.dudev.orderservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Обёртка над {@link JsonProcessingException}, возникающая,
 * когда невозможно десериализовать входящий JSON‑payload.
 *
 * @see JsonProcessingException
 * @author ussdanil
 * @since 1.0.0
 */

public class DeserializationException extends RuntimeException{

    public DeserializationException(String payload, JsonProcessingException cause) {
        super("Failed to deserialize payload: " + payload, cause);
    }
}
