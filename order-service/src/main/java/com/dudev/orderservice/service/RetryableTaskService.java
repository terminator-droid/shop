package com.dudev.orderservice.service;

import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.model.enums.TaskType;

import java.util.List;

/**
 * Сервис для создания, поиска и изменения статуса {@link RetryableTask}.
 *
 * <p>Используется планировщиком и процессорами задач.
 *
 * @see RetryableTask
 * @since 1.0.0
 */

public interface RetryableTaskService {

    void createTasks(RetryableTask... retryableTask);

    List<RetryableTask> findTasksForProcessing(TaskType taskType);

    void changeStatus(List<RetryableTask> successRetryableTasks, TaskStatus taskStatus);
}
