package com.dudev.apigateway.service.impl;


import com.dudev.apigateway.api.dto.UserCreateRequest;
import com.dudev.apigateway.api.dto.UserResponse;
import com.dudev.apigateway.exception.UserNotFoundException;
import com.dudev.apigateway.mapper.UserMapper;
import com.dudev.apigateway.repository.UserRepository;
import com.dudev.apigateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация {@link com.dudev.apigateway.service.UserService} и
 * {@link org.springframework.security.core.userdetails.UserDetailsService}.
 *
 * <p>Осуществляет CRUD‑операции над сущностью {@link com.dudev.apigateway.model.User},
 * а также предоставляет данные для Spring Security (UserDetails). Основные методы:
 * <ul>
 *   <li>{@code createUser} – преобразует DTO в сущность, кодирует пароль и сохраняет.</li>
 *   <li>{@code findByUserId}/{@code findByUsername} – поиск пользователя, бросает
 *       {@link com.dudev.apigateway.exception.UserNotFoundException} при отсутствии.</li>
 *   <li>{@code findAll} – возвращает список всех пользователей в виде DTO.</li>
 *   <li>{@code deleteById} – удаляет пользователя по UUID.</li>
 *   <li>{@code updatePassword} – меняет пароль, пере‑кодируя его.</li>
 *   <li>{@code loadUserByUsername} – возвращает объект {@link org.springframework.security.core.userdetails.User},
 *       используемый Spring Security для аутентификации.</li>
 * </ul>
 *
 * Для преобразования между DTO и сущностью используется {@link com.dudev.apigateway.mapper.UserMapper},
 * а пароли хешируются {@link org.springframework.security.crypto.password.PasswordEncoder}.
 *
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        return Optional.of(userCreateRequest)
                .map(it -> {
                    var user = userMapper.toEntity(userCreateRequest);
                    user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
                    return user;
                })
                .map(userRepository::save)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    @Override
    public UserResponse findByUserId(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse updatePassword(UUID id, String password) {
        return userRepository.findById(id)
                .map(it -> {
                    it.setPassword(passwordEncoder.encode(password));
                    return it;
                })
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public com.dudev.apigateway.model.User getCurrentUser() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(username, user.getPassword(), List.of(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user with username: " + username));
    }

}
