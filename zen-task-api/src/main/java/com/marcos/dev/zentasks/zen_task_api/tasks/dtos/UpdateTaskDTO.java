package com.marcos.dev.zentasks.zen_task_api.tasks.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTaskDTO(
        // Se o título for enviado, ele deve ter entre 3 e 300 caracteres.
        // Se for nulo, a validação é ignorada.
        @Size(min = 3, max = 300, message = "O título deve ter entre 3 e 300 caracteres.")
        String title,

        @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres.")
        String description,

        @FutureOrPresent(message = "A data de término deve ser hoje ou uma data futura.")
        LocalDate dueDate,

        Boolean isUrgent,
        Boolean isImportant,
        Boolean isCompleted
) {
}