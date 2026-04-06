package com.dudev.orderservice.configuration;

import com.dudev.orderservice.interceptor.GrpcClientInterceptor;
import com.example.grpc.InventoryServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация gRPC‑клиента для обращения к микросервису Inventory.
 *
 * <p>Создаёт {@link ManagedChannel} и бин {@link InventoryServiceGrpc.InventoryServiceBlockingStub},
 * который будет использовать {@link GrpcClientInterceptor} для передачи trace‑id,
 * API‑ключа и данных пользователя.
 *
 * @author ussdanil
 * @since 1.0.0
 */

@Configuration
@RequiredArgsConstructor
public class GrpcConfiguration {

    @Value("${grpc.client.inventory.address}")
    private String inventoryServiceAddress;
    @Value("${grpc.client.inventory.port}")
    private int inventoryServicePort;
    private final GrpcClientInterceptor interceptor;

    @Bean
    @Qualifier("inventoryChannel")
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress(inventoryServiceAddress, inventoryServicePort)
                .intercept(interceptor)
                .usePlaintext()
                .build();
    }

    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub(@Qualifier("inventoryChannel") ManagedChannel managedChannel) {
        return InventoryServiceGrpc.newBlockingStub(managedChannel);
    }
}