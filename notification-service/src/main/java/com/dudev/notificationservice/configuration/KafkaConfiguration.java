package com.dudev.notificationservice.configuration;

import com.dudev.kafka.dto.OrderDto;
import com.dudev.notificationservice.util.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka‑консюмера, используемого для получения событий
 * о созданных заказах.
 *
 * <p>Настройки берутся из {@link com.dudev.notificationservice.util.KafkaProperties}.
 * Консюмер использует {@link org.springframework.kafka.support.serializer.JacksonJsonDeserializer}
 * с включённым механизмом доверенных пакетов, что упрощает десериализацию
 * {@link com.dudev.kafka.dto.OrderDto}.
 *
 * @see com.dudev.notificationservice.util.KafkaProperties
 * @since 1.0.0
 */

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> manualAckFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDto> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL);
        containerFactory.setConsumerFactory(consumerFactory());
        return containerFactory;
    }

    private ConsumerFactory<String, OrderDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumer().getEnableAutoCommit());

        JacksonJsonDeserializer<OrderDto> orderDtoJacksonJsonDeserializer = new JacksonJsonDeserializer<>(OrderDto.class, true);
        orderDtoJacksonJsonDeserializer.addTrustedPackages(kafkaProperties.getConsumer().getTrustedPackages());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), orderDtoJacksonJsonDeserializer);
    }
}
