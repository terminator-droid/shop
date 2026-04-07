package com.dudev.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private UUID id;
    private UUID userId;
    private List<OrderItemDto> orderItemDtos;
    private double totalPrice;
    private LocalDateTime createdAt;
}
