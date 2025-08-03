package com.marcos.dev.zentasks.zen_task_api.tasks.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponseDTO(
        String title,
        String description,
        LocalDate dueDate,
        boolean isUrgent,
        boolean isImportant,
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime completedAt
) {
}
