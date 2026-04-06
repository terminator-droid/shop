package com.dudev.apigateway.model.enums;

import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN, USER;


    @Override
    public @NotNull String getAuthority() {
        return "Role_" + name();
    }
}
