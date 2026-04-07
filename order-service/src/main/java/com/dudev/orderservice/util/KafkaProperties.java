package com.dudev.orderservice.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Producer producer;

    @Data
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
    }
}
