package com.dudev.orderservice.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "retryable-tasks")
@RequiredArgsConstructor
public class RetryableTaskProperties {

    private final Integer limit;
    private final Integer timeout;
}
