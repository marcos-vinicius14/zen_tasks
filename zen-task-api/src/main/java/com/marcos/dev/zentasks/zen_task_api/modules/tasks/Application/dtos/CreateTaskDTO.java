package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CreateTaskDTO(
    @NotBlank(message = "O título não pode estar em branco.") @Size(min = 3, max = 300, message = "O título deve ter entre 3 e 300 caracteres.") String title,

    @NotBlank(message = "A descrição não pode estar em branco.") @Size(min = 3, max = 1000, message = "A descrição deve ter entre 3 e 1000 caracteres.") String description,

    @FutureOrPresent(message = "A data de término deve ser hoje ou uma data futura.") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dueDate,

    boolean isUrgent,
    boolean isImportant

) {
}
