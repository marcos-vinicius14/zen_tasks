package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

import java.util.Collections;
import java.util.Map;

public class UserValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public UserValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }
}