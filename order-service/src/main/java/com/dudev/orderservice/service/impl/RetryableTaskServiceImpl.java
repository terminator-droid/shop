package com.dudev.orderservice.service.impl;

import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.model.enums.TaskType;
import com.dudev.orderservice.repository.RetryableTaskRepository;
import com.dudev.orderservice.service.RetryableTaskService;
import com.dudev.orderservice.util.RetryableTaskProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса управления отложенными задачами.
 *
 * <p>Позволяет сохранять новые задачи, находить те, которые готовы к
 * обработке (статус {@link com.dudev.orderservice.model.enums.TaskStatus#IN_PROGRESS}),
 * а также менять их статус после выполнения.
 *
 * <p>Лимит количества задач, извлекаемых за один запрос, и таймаут
 * повторных попыток задаются в {@link com.dudev.orderservice.util.RetryableTaskProperties}.
 *
 * @see com.dudev.orderservice.service.RetryableTaskService
 * @see com.dudev.orderservice.model.RetryableTask
 * @since 1.0.0
 */

@Service
@RequiredArgsConstructor
public class RetryableTaskServiceImpl implements RetryableTaskService {

    private final RetryableTaskRepository retryableTaskRepository;
    private final RetryableTaskProperties retryableTaskProperties;


    @Transactional
    @Override
    public void createTasks(RetryableTask... retryableTask) {
        retryableTaskRepository.saveAll(Arrays.stream(retryableTask).toList());
    }

    @Transactional
    @Override
    public List<RetryableTask> findTasksForProcessing(TaskType taskType) {
        List<RetryableTask> tasksForProcessing = retryableTaskRepository.findTasksForProcessingByTaskType(retryableTaskProperties.getLimit(), taskType, Instant.now(), TaskStatus.IN_PROGRESS);
        tasksForProcessing.forEach(retryableTask -> retryableTask.setRetryTime(Instant.now().plus(Duration.of(retryableTaskProperties.getTimeout(), ChronoUnit.SECONDS))));
        return tasksForProcessing;
    }

    @Transactional
    @Override
    public void changeStatus(List<RetryableTask> retryableTasks, TaskStatus taskStatus) {
        List<UUID> taskIds = retryableTasks.stream().map(RetryableTask::getId).toList();
        retryableTaskRepository.changeRetryableTasksStatus(taskIds, taskStatus);
    }
}