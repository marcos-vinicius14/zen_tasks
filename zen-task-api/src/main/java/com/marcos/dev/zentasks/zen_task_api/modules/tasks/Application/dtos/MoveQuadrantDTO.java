package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import jakarta.validation.constraints.NotNull;

public record MoveQuadrantDTO(
    @NotNull(message = "O novo quadrante não pode ser nulo.")
    Quadrant newQuadrant) {

}
