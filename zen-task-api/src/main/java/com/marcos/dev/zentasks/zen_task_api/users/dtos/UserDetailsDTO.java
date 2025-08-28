package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;

public record UserDetailsDTO(
    UUID id,
    String username,
    String email,
    UserRole role,
    LocalDateTime createdAt) {
}
