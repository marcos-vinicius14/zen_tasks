package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

/**
 * This exception is thrown to indicate that a bad request has been made to the application.
 * It typically represents invalid or malformed requests that fail to meet the application's
 * expected criteria or constraints.
 *
 * This exception extends {@code RuntimeException}, making it an unchecked exception.
 * It can be used in scenarios where client-side errors need to be signaled, such as
 * violations of input validation, incompatible parameters, or other invalid conditions.
 *
 * The exception can be created with a descriptive error message to provide more context
 * about the nature of the bad request. Additionally, it can encapsulate a root cause
 * through its constructor to provide further debugging information.
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
