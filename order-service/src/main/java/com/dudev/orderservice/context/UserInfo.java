package com.dudev.orderservice.context;

import java.util.UUID;

public record UserInfo(UUID userId, String username) {
}