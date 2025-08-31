package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service;

import org.apache.coyote.BadRequestException;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;

public interface UserServiceInterface {

  RegistrationResultDTO createUser(RegisterDTO dto);

  AuthenticationResultDTO userAuthentication(AuthenticationDTO dto) throws BadRequestException;

  void changePassword(ChangePasswordRequest request);
}
