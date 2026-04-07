package com.dudev.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private UUID id;
    private String name;
    private int quantity;
    private double price;
    private double subtotal;
}
