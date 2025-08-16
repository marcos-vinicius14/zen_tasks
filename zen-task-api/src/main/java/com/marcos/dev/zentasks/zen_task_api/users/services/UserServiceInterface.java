package com.marcos.dev.zentasks.zen_task_api.users.services;

import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegistrationResultDTO;

public interface UserServiceInterface {

  RegistrationResultDTO createUser(RegisterDTO dto);

  AuthenticationResultDTO userAuthentication(AuthenticationDTO dto);

  void changePassword(ChangePasswordRequest request);
}
