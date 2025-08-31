package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;

public record TaskResponseDTO(
    Long id,
    String title,
    String description,
    TaskStatus taskStatus) {
}
