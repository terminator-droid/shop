package com.dudev.notificationservice.repository;

import com.dudev.notificationservice.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для доступа к {@link com.dudev.notificationservice.model.OrderItem}.
 *
 * <p>Наследует базовые CRUD‑операции Spring Data JPA и предоставляет
 * кастомный метод поиска всех позиций по идентификатору заказа.
 *
 * @see com.dudev.notificationservice.model.OrderItem
 * @since 1.0.0
 */

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findAllByOrderId(UUID orderId);
}
