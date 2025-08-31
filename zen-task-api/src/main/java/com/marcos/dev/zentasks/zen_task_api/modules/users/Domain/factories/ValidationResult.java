package com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private final Map<String, String> errors = new HashMap<>();

    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }
}