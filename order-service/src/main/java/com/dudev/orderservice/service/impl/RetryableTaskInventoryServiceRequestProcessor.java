package com.dudev.orderservice.service.impl;

import com.dudev.orderservice.dto.WriteOffProductsRequestDto;
import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.service.RetryableTaskProcessor;
import com.dudev.orderservice.service.RetryableTaskService;
import com.example.grpc.InventoryServiceGrpc;
import com.example.grpc.ProductWriteOffRequest;
import com.example.grpc.ProductsWriteOffRequest;
import com.example.grpc.ProductsWriteOffResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик отложенных задач, отвечающий за списание товаров в Inventory‑service.
 *
 * <p>Получает {@link RetryableTask} c типом {@link com.dudev.orderservice.model.enums.TaskType#SEND_WRITE_OFF_EVENT_TO_INVENTORY},
 * десериализует payload в {@link com.dudev.orderservice.dto.WriteOffProductsRequestDto},
 * формирует gRPC‑запрос {@code ProductsWriteOffRequest} и отправляет его
 * в Inventory‑service. При ошибке gRPC возвращает {@code false},
 * что приводит к повторной попытке в следующем цикле планировщика.
 *
 * @see com.dudev.orderservice.service.RetryableTaskProcessor
 * @see com.dudev.orderservice.service.RetryableTaskService
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class RetryableTaskInventoryServiceRequestProcessor implements RetryableTaskProcessor {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub;
    private final ObjectMapper objectMapper;
    private final RetryableTaskService retryableTaskService;

    @Override
    public void process(List<RetryableTask> retryableTasks) {
        List<RetryableTask> successRetryableTasks = new ArrayList<>();
        retryableTasks.parallelStream()
                .forEach(retryableTask -> {
                    try {
                        boolean success = writeOff(retryableTask);
                        if (success) {
                            successRetryableTasks.add(retryableTask);
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
        retryableTaskService.changeStatus(successRetryableTasks, TaskStatus.COMPLETED);
    }

    private boolean writeOff(RetryableTask retryableTask) throws JsonProcessingException {
        try {
            WriteOffProductsRequestDto writeOffProductsRequestDto = objectMapper.readValue(retryableTask.getPayload(), WriteOffProductsRequestDto.class);
            ProductsWriteOffRequest writeOffRequest = ProductsWriteOffRequest.newBuilder()
                    .addAllProducts(writeOffProductsRequestDto.getWriteOffProductRequestDtos().stream()
                            .map(it -> ProductWriteOffRequest.newBuilder()
                                    .setQuantity(it.getQuantity())
                                    .setId(it.getProductId())
                                    .build())
                            .toList())
                    .build();
            ProductsWriteOffResponse productsWriteOffResponse = inventoryServiceBlockingStub.writeOff(writeOffRequest);
        } catch (StatusRuntimeException ex) {
            return false;
        }
        return true;
    }
}
