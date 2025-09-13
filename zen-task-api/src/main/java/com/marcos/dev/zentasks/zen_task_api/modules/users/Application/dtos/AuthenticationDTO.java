package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O nome de usuário não pode estar em branco.")
        String username,
        
        @NotBlank(message = "A senha não pode estar em branco.")
        String password
) {
}
