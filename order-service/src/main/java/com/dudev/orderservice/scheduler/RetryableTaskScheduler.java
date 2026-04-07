package com.dudev.orderservice.scheduler;

import com.dudev.orderservice.context.UserContextHolder;
import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskType;
import com.dudev.orderservice.service.RetryableTaskProcessor;
import com.dudev.orderservice.service.RetryableTaskService;
import com.dudev.orderservice.service.impl.RetryableTaskInventoryServiceRequestProcessor;
import com.dudev.orderservice.service.impl.RetryableTaskNotificationServiceRequestProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Планировщик, запускающий обработку отложенных задач каждые 5сек.
 *
 * <p>Для каждого {@link TaskType} подбирает соответствующий
 * {@link RetryableTaskProcessor} и передаёт ему найденные задачи.
 *
 * @see RetryableTaskProcessor
 * @see TaskType
 * @since 1.0.0
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class RetryableTaskScheduler {

    private final RetryableTaskService retryableTaskService;
    private final RetryableTaskNotificationServiceRequestProcessor retryableTaskNotificationServiceRequestProcessor;
    private final RetryableTaskInventoryServiceRequestProcessor retryableTaskInventoryServiceRequestProcessor;
    @Value("${scheduler.system-username}")
    private String systemUsername;
    @Value("${scheduler.system-user-id}")
    private UUID systemUserId;

    @Scheduled(fixedRate = 5000)
    public void executeRetryableTasks() {
        UserContextHolder.setUser(systemUserId, systemUsername);

        Map<TaskType, RetryableTaskProcessor> retryableTaskProcessorMap = Map.of(
                TaskType.SEND_ORDER_CREATED_EVENT_TO_NOTIFICATION, retryableTaskNotificationServiceRequestProcessor,
                TaskType.SEND_WRITE_OFF_EVENT_TO_INVENTORY, retryableTaskInventoryServiceRequestProcessor);

        log.info("Start processing tasks");
        retryableTaskProcessorMap.forEach((key, value) -> {
            List<RetryableTask> tasksForProcessing = retryableTaskService.findTasksForProcessing(key);
            value.process(tasksForProcessing);
        });
    }
}