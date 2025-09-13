package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
    @NotBlank(message = "O nome de usuário não pode estar em branco.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    String username,
    
    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "O email deve ter um formato válido.")
    String email,
    
    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    String password,
    
    @NotNull(message = "O papel do usuário não pode ser nulo.")
    UserRole role) {

  public RegisterDTO(String username, String email, String password) {
    this(username, email, password, UserRole.USER);
  }
}
