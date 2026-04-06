package com.dudev.notificationservice.service;

import java.util.UUID;

public interface JwtService {

    String extractUserName(String token);

    UUID extractUserId(String token);

}
