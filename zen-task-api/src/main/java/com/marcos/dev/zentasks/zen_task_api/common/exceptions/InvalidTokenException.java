package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}