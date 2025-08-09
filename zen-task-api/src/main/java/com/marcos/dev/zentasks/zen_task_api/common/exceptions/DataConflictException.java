package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

public class DataConflictException extends RuntimeException{
    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
