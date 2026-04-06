package com.dudev.notificationservice.repository;

import com.dudev.notificationservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для доступа к {@link com.dudev.notificationservice.model.Order}.
 *
 * <p>Содержит метод поиска заказов по идентификатору пользователя.
 *
 * @see com.dudev.notificationservice.model.Order
 * @since 1.0.0
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);
}
