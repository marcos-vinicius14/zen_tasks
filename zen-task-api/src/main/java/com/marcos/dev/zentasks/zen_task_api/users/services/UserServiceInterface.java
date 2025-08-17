package com.marcos.dev.zentasks.zen_task_api.users.services;

import org.apache.coyote.BadRequestException;

import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegistrationResultDTO;

public interface UserServiceInterface {

  RegistrationResultDTO createUser(RegisterDTO dto);

  AuthenticationResultDTO userAuthentication(AuthenticationDTO dto) throws BadRequestException;

  void changePassword(ChangePasswordRequest request);
}
