package com.marcos.dev.zentasks.zen_task_api.users.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.DataConflictException;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.users.mappers.UserMapper;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.users.repository.UserRepository;

@Service
public class UserServiceImpl implements UserServiceInterface {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(
      UserRepository userRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public RegistrationResultDTO createUser(RegisterDTO data) {
    // TODO: Email e username devem ser unicos
    // TODO: A senha deve ser criptografada antes de persistir o users

    if (userRepository.existsByEmail(data.email())) {
      throw new DataConflictException("Email já em uso");
    }

    if (userRepository.existsByUsername(data.username())) {
      throw new DataConflictException("Nome de usuário já em uso");
    }

    String encrypetedPassword = new BCryptPasswordEncoder().encode(data.password());

    UserModel user = UserFactory.create(
        data.username(),
        data.email(),
        encrypetedPassword,
        data.role());

    userRepository.save(user);

    return userMapper.toResultDTO(user);
  }

  @Override
  public AuthenticationResultDTO userAuthentication(AuthenticationDTO dto) {
    // TODO Auto-generated method stub
    // TODO: Gerar o token se a autenticação for bem sucedida
    // TODO: Verificar se o user existe antes de prosseguir com a autenticação
    throw new UnsupportedOperationException("Unimplemented method 'userAuthentication'");
  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

}
