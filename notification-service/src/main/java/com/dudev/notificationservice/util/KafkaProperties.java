package com.dudev.notificationservice.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private Consumer consumer = new Consumer();
    private String bootstrapServers;

    @Data
    public static class Consumer {
        private String enableAutoCommit;
        private String keyDeserializer;
        private String valueDeserializer;
        private String groupId;
        private String autoOffsetReset;
        private String trustedPackages;
    }
}
