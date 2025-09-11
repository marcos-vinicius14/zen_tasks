package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.DataConflictException;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.ChangePasswordRequest;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.mappers.UserMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserServiceInterface {

  private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  public UserServiceImpl(
      UserRepository userRepository,
      UserMapper userMapper,
      AuthenticationManager authenticationManager,
      TokenService tokenService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  @Override
  @Transactional
  public RegistrationResultDTO createUser(RegisterDTO data) {
    if (userRepository.existsByEmail(data.email())) {
      throw new DataConflictException("Email já em uso");
    }

    if (userRepository.existsByUsername(data.username())) {
      throw new DataConflictException("Nome de usuário já em uso");
    }

    UserModel user = UserFactory.create(
        data.username(),
        data.email(),
        data.password(),
        data.role());

    userRepository.save(user);

    return userMapper.toResultDTO(user);
  }

  @Override
  public AuthenticationResultDTO userAuthentication(AuthenticationDTO dto) {

    logger.info("Iniciando tentativa de autenticação para {}", dto.username());

    if (dto.username().isBlank() || dto.password().isBlank()) {
      logger.warn("Erro na autenticação. Campos de login vazios para {}", dto.username());
      throw new InvalidInputException("Nome de usuário ou senha não podem estar vazios");
    }

    if (!userRepository.existsByUsername(dto.username())) {
      throw new ResourceNotFoundException("Usuário não encontrado");
    }

    UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(dto.username(),
        dto.password());

    try {

      var auth = authenticationManager.authenticate(userNamePassword);

      String token = tokenService.generateToken((UserModel) auth.getPrincipal());

      return new AuthenticationResultDTO(token);

    } catch (DisabledException e) {
      logger.warn("Usuário desativado: {}", dto.username(), e);
      throw new AuthenticationFailedException("Usuário desativado", e);
    } catch (AuthenticationException e) {
      logger.error("Falha geral na autenticação para usuário: {}", dto.username(), e);
      throw new AuthenticationFailedException("Falha na autenticação", e);
    } catch (Exception e) {
      logger.error("Erro inesperado na autenticação para usuário: {}", dto.username(), e);
      throw new RuntimeException("Erro interno na autenticação", e);
    }

  }

  @Override
  public void changePassword(ChangePasswordRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

}
