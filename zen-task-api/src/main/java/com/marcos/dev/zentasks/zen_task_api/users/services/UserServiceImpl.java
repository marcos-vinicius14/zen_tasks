package com.marcos.dev.zentasks.zen_task_api.users.services;

import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegistrationResultDTO;

@Service
public class UserServiceImpl implements UserServiceInterface {

  public RegistrationResultDTO createUser(RegisterDTO data) {
    // TODO: Email e username devem ser unicos
    // TODO: A senha deve ser criptografada antes de persistir o users
    return null;
  }

  @Override
  public AuthenticationResultDTO userAuthentication(AuthenticationDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'userAuthentication'");
  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

}
