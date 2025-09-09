package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;

public record MoveQuadrantDTO(
    Long taskId,
    Quadrant newQuadrant
) {

    
}
