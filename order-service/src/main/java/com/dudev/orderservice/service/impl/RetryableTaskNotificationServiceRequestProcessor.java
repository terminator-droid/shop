package com.dudev.orderservice.service.impl;

import com.dudev.kafka.dto.OrderCreateEvent;
import com.dudev.orderservice.exception.DeserializationException;
import com.dudev.orderservice.kafka.OrderProducer;
import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.service.RetryableTaskProcessor;
import com.dudev.orderservice.service.RetryableTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Обработчик отложенных задач, отвечающий за отправку события о созданном заказе
 * в Notification‑service через Kafka.
 *
 * <p>Десериализует payload задачи в {@link com.dudev.kafka.dto.OrderCreateEvent},
 * отправляет его с помощью {@link com.dudev.orderservice.kafka.OrderProducer}
 * и ждёт подтверждения отправки (таймаут 5 сек). При любом сбое задача считается
 * не выполненной и будет повторена планировщиком.
 *
 * @see com.dudev.orderservice.service.RetryableTaskProcessor
 * @see com.dudev.orderservice.service.RetryableTaskService
 * @see com.dudev.orderservice.kafka.OrderProducer
 * @since 1.0.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryableTaskNotificationServiceRequestProcessor implements RetryableTaskProcessor {

    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper;
    private final RetryableTaskService retryableTaskService;

    @Override
    public void process(List<RetryableTask> retryableTasks) {
        List<RetryableTask> successTasks = new ArrayList<>();
        retryableTasks.parallelStream()
                .forEach(retryableTask -> {
                    boolean successProceed = processTask(retryableTask);
                    if (successProceed) {
                        successTasks.add(retryableTask);
                    }
                });
        retryableTaskService.changeStatus(successTasks, TaskStatus.COMPLETED);
    }


    private boolean processTask(RetryableTask retryableTask) {
        OrderCreateEvent orderCreateEvent = tryParseTask(retryableTask, OrderCreateEvent.class);
        try {
            SendResult<String, OrderCreateEvent> sendResult = orderProducer.sendOrderCreated(orderCreateEvent)
                    .get(5, TimeUnit.SECONDS);
            log.debug("Event {} sent to {}@{} offset={}",
                    orderCreateEvent.getCorrelationId(),
                    sendResult.getRecordMetadata().topic(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().offset()
            );
            return true;
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.warn("Kafka timeout for event {}", orderCreateEvent.getCorrelationId());
            return false;
        }
    }

    private <T> T tryParseTask(RetryableTask retryableTask, Class<T> clazz) {
        try {
            return objectMapper.readValue(retryableTask.getPayload(), clazz);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(retryableTask.getPayload(), e);
        }
    }
}