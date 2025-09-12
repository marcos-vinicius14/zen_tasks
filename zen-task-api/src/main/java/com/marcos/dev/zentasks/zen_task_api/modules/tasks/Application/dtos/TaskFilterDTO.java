package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;

public record TaskFilterDTO(
    Quadrant quadrant,
    TaskStatus status,

    @JsonFormat(pattern = "dd-MM-yyyy") LocalDate fromDate,

    @JsonFormat(pattern = "dd-MM-yyyy") LocalDate toDate,

    Boolean isComplete) {
}
