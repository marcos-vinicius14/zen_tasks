package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private final int statusCode;
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;

    public ErrorResponseDTO(int statusCode, String message, String details, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
