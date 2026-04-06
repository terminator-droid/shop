package com.dudev.inventoryservice.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "token")
@RequiredArgsConstructor
public class TokenProperties {

    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private String salt;
    }
}
