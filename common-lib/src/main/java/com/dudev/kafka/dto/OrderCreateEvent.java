package com.dudev.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateEvent {

    private OrderDto orderDto;
    private UUID correlationId;
}