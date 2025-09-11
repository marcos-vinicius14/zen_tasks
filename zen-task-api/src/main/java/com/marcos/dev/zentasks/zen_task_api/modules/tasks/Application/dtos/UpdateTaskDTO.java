package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

public record UpdateTaskDTO(
    @Size(min = 3, max = 300, message = "O título deve ter entre 3 e 300 caracteres.") String title,

    @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres.") String description,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresent(message = "A data de término deve ser hoje ou uma data futura.")
    LocalDate dueDate,

    Boolean isUrgent,
    Boolean isImportant,
    Boolean isCompleted) {
}
