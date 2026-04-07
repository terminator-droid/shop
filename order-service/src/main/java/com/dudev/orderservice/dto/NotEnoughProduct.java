package com.dudev.orderservice.dto;

import com.dudev.orderservice.exception.NotEnoughProductsException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Информация о товаре, которого недостаточно на складе.
 *
 * <p>Используется в {@link NotEnoughProductsException} для формирования
 * детального сообщения об ошибке.
 *
 * @see NotEnoughProductsException
 * @author ussdanil
 * @since 1.0.0
 */

@Data
@RequiredArgsConstructor
public class NotEnoughProduct {

    private final String productDescription;
    private final int requestedQuantity;
    private final int availableQuantity;
}