package com.marcos.dev.zentasks.zen_task_api.users.services;

public class AuthenticationFailedException extends RuntimeException {

  public AuthenticationFailedException(String message) {
    super(message);
  }

  public AuthenticationFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
