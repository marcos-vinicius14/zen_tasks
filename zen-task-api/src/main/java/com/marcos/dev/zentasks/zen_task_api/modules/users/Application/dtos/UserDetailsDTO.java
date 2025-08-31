package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;

public record UserDetailsDTO(
    UUID id,
    String username,
    String email,
    UserRole role,
    LocalDateTime createdAt) {
}
