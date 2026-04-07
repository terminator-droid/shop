package com.dudev.orderservice.model;

import com.dudev.orderservice.model.enums.TaskStatus;
import com.dudev.orderservice.model.enums.TaskType;
import com.dudev.orderservice.scheduler.RetryableTaskScheduler;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность, представляющая задачу, которую необходимо выполнить повторно.
 *
 * <p>Сохраняет сериализованный payload, статус, тип задачи и запланированное
 * время повторения. Используется планировщиком {@link RetryableTaskScheduler}.
 *
 * @see TaskStatus
 * @see TaskType
 * @since 1.0.0
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "retryable_tasks")
public class RetryableTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String payload;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    private Instant retryTime;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;
}