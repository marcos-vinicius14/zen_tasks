package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;

import java.time.LocalDateTime;

/**
 * DTO para retorno do resultado da autenticação
 */
public record AuthenticationResultDTO(
        String token,
        String username,
        String email,
        UserRole role,
        LocalDateTime lastLogin
) {

}