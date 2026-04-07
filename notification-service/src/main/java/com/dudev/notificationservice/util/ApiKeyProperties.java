package com.dudev.notificationservice.util;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@ConfigurationProperties(prefix = "api.keys")
@Data
@Validated
public class ApiKeyProperties {

    @NotNull
    private String orderService;
    @NotNull
    private String inventoryService;

    @NotNull
    private String defaultService;

    public Set<String> getApiKeys() {
        return Set.of(orderService, inventoryService, defaultService);
    }
}