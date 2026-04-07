package com.dudev.apigateway.service;

import com.dudev.apigateway.api.dto.UserCreateRequest;
import com.dudev.apigateway.api.dto.UserResponse;
import com.dudev.apigateway.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

/**
 * Сервис работы с пользователями (CRUD, поиск, обновление пароля).
 *
 * @since 1.0.0
 */

public interface UserService extends UserDetailsService {

    UserResponse createUser(UserCreateRequest userCreateRequest);

    UserResponse findByUserId(UUID userId);

    UserResponse findByUsername(String username);

    List<UserResponse> findAll();

    void deleteById(UUID id);

    UserResponse updatePassword(UUID id, @NotNull String password);

    User getCurrentUser();
}
