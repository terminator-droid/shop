package com.dudev.inventoryservice.exception;

/**
 * Исключение, бросаемое, когда запросом запрашивается товар, который не найден.
 *
 * @since 1.0.0
 */

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Product not found with id = " + productId);
    }
}
