package com.dudev.apigateway.controller;

import com.dudev.apigateway.api.UsersApi;
import com.dudev.apigateway.api.dto.UserCreateRequest;
import com.dudev.apigateway.api.dto.UserResponse;
import com.dudev.apigateway.api.dto.UserUpdatePasswordRequest;
import com.dudev.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST‑контроллер, представляющий пользовательские операции
 * (создание, удаление, поиск, обновление пароля) через слой сервиса
 * {@link com.dudev.apigateway.service.UserService}.
 *
 * @see com.dudev.apigateway.service.UserService
 * @since 1.0.0
 */

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> createUser(UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.createUser(userCreateRequest));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        return ResponseEntity.ok(userService.findByUserId(id));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Override
    public ResponseEntity<UserResponse> updatePassword(UUID id, UserUpdatePasswordRequest userUpdatePasswordRequest) {
        return ResponseEntity.ok(userService.updatePassword(id, userUpdatePasswordRequest.getPassword()));
    }
}
