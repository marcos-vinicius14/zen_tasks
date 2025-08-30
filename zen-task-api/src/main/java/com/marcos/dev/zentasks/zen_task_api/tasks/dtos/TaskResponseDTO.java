package com.marcos.dev.zentasks.zen_task_api.tasks.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.marcos.dev.zentasks.zen_task_api.tasks.enums.Quadrant;

public record TaskResponseDTO(
    Long id, String title,
    String description,
    LocalDate dueDate,
    boolean isUrgent,
    boolean isImportant,
    boolean isCompleted,
    Quadrant quadrant,
    LocalDateTime createdAt,
    LocalDateTime completedAt) {
}
