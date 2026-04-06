package com.dudev.orderservice.configuration;

import com.dudev.kafka.dto.OrderCreateEvent;
import com.dudev.orderservice.util.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka‑продюсера, отправляющего события о создании заказа.
 *
 * <p>Параметры берутся из {@link KafkaProperties}. Продюсер использует
 * {@link JacksonJsonSerializer} с включённой информацией о типе,
 * что упрощает десериализацию на стороне потребителя.
 *
 * @see KafkaProperties
 * @author ussdanil
 * @since 1.0.0
 */

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, OrderCreateEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());

        JacksonJsonSerializer<OrderCreateEvent> objectJacksonJsonSerializer = new JacksonJsonSerializer<>();
        objectJacksonJsonSerializer.setAddTypeInfo(true);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(),
                objectJacksonJsonSerializer);
    }

    @Bean
    public KafkaTemplate<String, OrderCreateEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
