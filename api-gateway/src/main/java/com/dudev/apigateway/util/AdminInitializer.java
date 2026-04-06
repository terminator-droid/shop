package com.dudev.apigateway.util;

import com.dudev.apigateway.model.User;
import com.dudev.apigateway.model.enums.Role;
import com.dudev.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.admin.password:admin123}")
    private String adminPassword;
    @Value("${app.admin.username:admin123}")
    private String adminUsername;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername(adminUsername)) {
            User user = User.builder()
                    .role(Role.ADMIN)
                    .password(passwordEncoder.encode(adminPassword))
                    .username(adminUsername)
                    .build();
            userRepository.save(user);
        }
    }
}
