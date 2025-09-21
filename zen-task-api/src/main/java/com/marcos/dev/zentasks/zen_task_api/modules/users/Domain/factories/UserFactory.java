package com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.UserValidationException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

public class UserFactory {
  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
  private static final int MIN_USERNAME_LENGTH = 3;

  public static UserModel create(String username, String email, String password) {
    validateInput(username, email, password);

    String passwordHash = new BCryptPasswordEncoder().encode(password);
    return new UserModel(username, email, passwordHash, UserRole.USER);
  }

  public static UserModel createAdmin(String username, String email, String password) {
    validateInput(username, email, password);
    String passwordHash = new BCryptPasswordEncoder().encode(password);
    return new UserModel(username, email, passwordHash, UserRole.ADMIN);
  }

  private static void validateInput(String username, String email, String password) {
    ValidationResult result = new ValidationResult();

    validateUsername(username, result);
    validateEmail(email, result);
    validatePassword(password, result);

    if (result.hasErrors()) {
      throw new UserValidationException("Invalid user data", result.getErrors());
    }
  }

  private static void validateUsername(String username, ValidationResult result) {
    if (username == null || username.isBlank()) {
      result.addError("username", "Username cannot be null or empty");
    } else if (username.length() < MIN_USERNAME_LENGTH) {
      result.addError("username", "Username must be at least " + MIN_USERNAME_LENGTH + " characters long");
    }
  }

  private static void validateEmail(String email, ValidationResult result) {
    if (email == null || !email.matches(EMAIL_REGEX)) {
      result.addError("email", "Invalid email format");
    }
  }

  private static void validatePassword(String password, ValidationResult result) {
    if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
      result.addError("password", "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
    }
  }

}
