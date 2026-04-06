package com.dudev.inventoryservice.exception;

/**
 * Исключение, бросаемое при попытке списать больше товаров, чем есть в наличии.
 *
 * <p>В дальнейшем может быть использовано в глобальном обработчике
 * {@link com.dudev.inventoryservice.handler.GlobalExceptionHandler}
 * для формирования ответа с HTTP‑статусом {@code 409 CONFLICT}.
 *
 * @since 1.0.0
 */

public class NotEnoughProductsException extends RuntimeException{

    public NotEnoughProductsException() {
        super("Not enough products");
    }
}
