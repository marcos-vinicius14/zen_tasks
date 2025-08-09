package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

/**
 * This exception is thrown when a business rule violation occurs within the application.
 * It is typically used to represent conditions that are specific to the domain logic
 * and are not allowed under the defined business rules.
 *
 * This is an unchecked exception since it extends {@code RuntimeException}.
 * It can be used to signal invalid operations or constraints that must be enforced
 * within the application's context.
 *
 * The exception is associated with a descriptive error message that provides details
 * about the business rule that was violated.
 */

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
