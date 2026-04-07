package com.dudev.orderservice;

import com.dudev.orderservice.util.ApiKeyProperties;
import com.dudev.orderservice.util.KafkaProperties;
import com.dudev.orderservice.util.RetryableTaskProperties;
import com.dudev.orderservice.util.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApiKeyProperties.class, KafkaProperties.class, TokenProperties.class, RetryableTaskProperties.class})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
