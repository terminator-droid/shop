package com.dudev.notificationservice;

import com.dudev.notificationservice.util.ApiKeyProperties;
import com.dudev.notificationservice.util.KafkaProperties;
import com.dudev.notificationservice.util.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class, ApiKeyProperties.class, TokenProperties.class})
@EnableKafka
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
