package com.marcos.dev.zentasks.zen_task_api.common.dtos;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponseDTO extends ErrorResponseDTO {

    private final Map<String, String> errors;

    public ValidationErrorResponseDTO(int statusCode, String message, String details, LocalDateTime timestamp, Map<String, String> errors) {
        super(statusCode, message, details, timestamp);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}