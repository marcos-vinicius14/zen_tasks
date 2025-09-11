package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import java.time.LocalDate;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;

public record TaskResponseDTO(
    Long id,
    String title,
    String description,
    LocalDate dueDate,
    TaskStatus taskStatus,
    Quadrant quadrant,
    boolean isCompleted

) {
}
