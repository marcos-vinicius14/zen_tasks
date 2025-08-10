package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para retorno do resultado do registro
 */
public record RegistrationResultDTO(
        String token,
        String username,
        String email,
        UserRole role,
        UUID userId,
        LocalDateTime createdAt
) {
    public static RegistrationResultDTO from(UserModel user, String token) {
        return new RegistrationResultDTO(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getId(),
                user.getCreatedAt()
        );
    }
}