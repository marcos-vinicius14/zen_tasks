package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.mappers;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserModel toEntity(RegisterDTO dto) {
    return UserFactory.create(
        dto.username(),
        dto.email(),
        dto.password(), dto.role());
  }

  public RegisterDTO toDTO(UserModel entity) {
    return new RegisterDTO(
        entity.getUsername(),
        entity.getEmail(),
        entity.getPasswordHash(),
        entity.getRole());
  }

  public RegistrationResultDTO toResultDTO(UserModel entity) {
    return new RegistrationResultDTO(
        entity.getUsername(),
        entity.getEmail(),
        entity.getRole(),
        entity.getId(),
        entity.getCreatedAt());
  }

}
