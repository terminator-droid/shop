package com.dudev.apigateway.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "token")
@RequiredArgsConstructor
public class TokenProperties {

    private Jwt jwt = new Jwt();
    private Refresh refresh = new Refresh();

    @Data
    public static class Jwt {
        private Integer expiration;
        private String salt;
    }

    @Data
    public static class Refresh {
        private Integer expiration;
    }
}
