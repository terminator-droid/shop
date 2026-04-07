package com.dudev.inventoryservice.service;

import java.util.UUID;

public interface JwtService {

    String extractUserName(String token);

    UUID extractUserId(String token);

}
