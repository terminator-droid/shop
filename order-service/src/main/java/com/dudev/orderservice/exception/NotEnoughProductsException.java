package com.dudev.orderservice.exception;

import com.dudev.orderservice.dto.NotEnoughProduct;

import java.util.List;

/**
 * Исключение, бросаемое, когда в запасе недостаточно требуемых товаров.
 *
 * <p>Содержит список {@link NotEnoughProduct} с деталями о каждом товаре.
 * Формирует человекочитаемое сообщение, которое передаётся клиенту.
 *
 * @see NotEnoughProduct
 * @author ussdanil
 * @since 1.0.0
 */

public class NotEnoughProductsException extends RuntimeException {

    private final List<NotEnoughProduct> products;
    private static final String NOT_ENOUGH_PRODUCTS = "Not enough products: ";

    public NotEnoughProductsException(List<NotEnoughProduct> notEnoughProducts) {
        super(getMessage(notEnoughProducts));
        this.products = notEnoughProducts;
    }

    private static String getMessage(List<NotEnoughProduct> notEnoughProducts) {
        if (notEnoughProducts == null || notEnoughProducts.isEmpty()) {
            return NOT_ENOUGH_PRODUCTS + "no details";
        }
        StringBuilder message = new StringBuilder(NOT_ENOUGH_PRODUCTS);
        notEnoughProducts.forEach(it -> message.append(String.format("product: %s, requested quantity: %d, available quantity: %d",
                it.getProductDescription(), it.getRequestedQuantity(), it.getAvailableQuantity()))
                .append("; "));
        return message.toString();
    }
}
