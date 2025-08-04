package com.marcos.dev.zentasks.zen_task_api.tasks.exceptions;


/**
 * This exception is thrown when a forbidden access attempt is detected within the application.
 * It typically indicates that a user or process tried to access a resource or perform an action
 * without the necessary permissions or authorization.
 *
 * This is an unchecked exception as it extends {@code RuntimeException}.
 * It can be used to represent security or access control violations, where the application
 * enforces restrictions on certain operations.
 *
 * The exception includes a descriptive error message that provides details about the
 * forbidden access scenario, making it easier to diagnose and handle the issue appropriately.
 */
public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message) {
        super(message);
    }

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
