package com.marcos.dev.zentasks.zen_task_api.users.controller;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.users.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.users.services.UserServiceInterface;

@RestController
@RequestMapping("/v1")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);
  private final UserServiceInterface userService;

  public UserController(UserServiceInterface userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResultDTO> login(@RequestBody AuthenticationDTO data) throws BadRequestException {

    logger.info("Iniciando a autenticação para o user: {}", data.username());

    AuthenticationResultDTO result = userService.userAuthentication(data);

    logger.debug("Login bem-sucedido para o user: {}", data.username());

    return ResponseEntity.ok(result);

  }

  @PostMapping("/register")
  public ResponseEntity<RegistrationResultDTO> register(@RequestBody RegisterDTO data) {
    logger.info("Iniciando a criação do user: {}", data.username());

    RegistrationResultDTO result = userService.createUser(data);
    logger.debug("Usuario {} criado com sucesso", data.username());

    return ResponseEntity.ok(result);
  }

}
