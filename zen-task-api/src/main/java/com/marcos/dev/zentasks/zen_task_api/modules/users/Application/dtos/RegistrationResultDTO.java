package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para retorno do resultado do registro
 */
public record RegistrationResultDTO(
    String username,
    String email,
    UserRole role,
    UUID userId,
    LocalDateTime createdAt) {

}
