package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidInputException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de alteração de senha
 */
public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String newPassword,

        @NotBlank(message = "Password confirmation is required")
        String passwordConfirmation
) {
    public ChangePasswordRequest {
        if (newPassword != null && !newPassword.equals(passwordConfirmation)) {
            throw new InvalidInputException("New password and confirmation do not match");
        }
    }
}