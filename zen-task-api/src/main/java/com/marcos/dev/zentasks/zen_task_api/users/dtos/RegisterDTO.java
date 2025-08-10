package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;

public record RegisterDTO(
        String username,
        String email,
        String password,
        UserRole role
) {
}
