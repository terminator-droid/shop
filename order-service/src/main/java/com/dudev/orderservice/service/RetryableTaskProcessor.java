package com.dudev.orderservice.service;

import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskType;

import java.util.List;

/**
 * Обработчик (процессор) отложенной задачи.
 *
 * <p>Каждая реализация получает список задач конкретного {@link TaskType}
 * и пытается выполнить их; в случае успеха статус меняется на {@code COMPLETED},
 * иначе — на {@code IN_PROCESSING}.
 *
 * @see TaskType
 * @since 1.0.0
 */

public interface RetryableTaskProcessor {

    void process(List<RetryableTask> retryableTasks);
}
