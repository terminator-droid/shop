package com.dudev.notificationservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA‑сущность, представляющая заказ.
 *
 * <p>Содержит информацию о пользователе, общей стоимости, времени создания
 * и списке позиций {@link OrderItem}. Связь с позициями реализована
 * как {@code one‑to‑many} с каскадным сохранением.
 *
 * @see OrderItem
 * @since 1.0.0
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;

    private UUID userId;

    private Double totalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> items;

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
        orderItem.setOrder(this);
    }
}
