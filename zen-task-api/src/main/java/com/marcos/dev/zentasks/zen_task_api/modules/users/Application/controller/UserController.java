package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.controller;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.UserDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service.UserServiceInterface;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1")
public class UserController {

        private static final Logger logger = LoggerFactory.getLogger(UserController.class);
        private final UserServiceInterface userService;

        public UserController(UserServiceInterface userService) {
                this.userService = userService;
        }

        @PostMapping("/login")
        public ResponseEntity<AuthenticationResultDTO> login(@Valid @RequestBody AuthenticationDTO data)
                        throws BadRequestException {
                logger.info("Iniciando a autenticação para o user: {}", data.username());

                AuthenticationResultDTO result = userService.userAuthentication(data);

                ResponseCookie cookie = ResponseCookie.from("jwt-token", result.token())
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(7 * 24 * 60 * 60)
                                .sameSite("Lax")
                                .build();

                logger.debug("Login bem-sucedido para o user: {}", data.username());

                return ResponseEntity.ok()
                                .header("Set-Cookie", cookie.toString())
                                .body(result);
        }

        @PostMapping("/register")
        public ResponseEntity<RegistrationResultDTO> register(@Valid @RequestBody RegisterDTO data) {
                logger.info("Iniciando a criação do user: {}", data.username());

                RegistrationResultDTO result = userService.createUser(data);
                logger.debug("Usuario {} criado com sucesso", data.username());

                // TODO: Should return 201 CREATED for proper REST compliance, but keeping 200
                // for backward compatibility
                return ResponseEntity.ok(result);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout() {
                ResponseCookie cookie = ResponseCookie.from("jwt-token", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.status(HttpStatus.OK)
                                .header("Set-Cookie", cookie.toString())
                                .build();

        }

        @GetMapping("/auth/status")
        public ResponseEntity<UserDTO> getAuthenticatedUser(
                        @AuthenticationPrincipal UserModel userDetails) {

                UserDTO result = new UserDTO(
                                userDetails.getId().toString(),
                                userDetails.getUsername(),
                                userDetails.getEmail());

                return ResponseEntity.ok(result);
        }

}
