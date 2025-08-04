package com.marcos.dev.zentasks.zen_task_api.tasks.dtos;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
}
