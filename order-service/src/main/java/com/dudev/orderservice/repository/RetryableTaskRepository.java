package com.dudev.orderservice.repository;

import com.dudev.orderservice.model.RetryableTask;
import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.model.enums.TaskType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для управления {@link RetryableTask}.
 *
 * <p>Содержит кастомные запросы для выборки задач,
 * готовых к обработке, и массового изменения их статуса.
 *
 * @see RetryableTask
 * @since 1.0.0
 */

@Repository
public interface RetryableTaskRepository extends CrudRepository<RetryableTask, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM RetryableTask t " +
            "where t.retryTime <= :retryTime " +
            "AND t.type = :taskType " +
            "AND t.status = :status " +
            "order by t.retryTime asc ")
    List<RetryableTask> findTasksForProcessingByTaskType(Integer limit, TaskType taskType, Instant retryTime, TaskStatus status);


    @Modifying
    @Query("UPDATE RetryableTask t SET t.status = :taskStatus " +
            "WHERE t.id in :retryableTasks")
    void changeRetryableTasksStatus(List<UUID> retryableTasks, TaskStatus taskStatus);
}