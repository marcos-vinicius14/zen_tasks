package com.marcos.dev.zentasks.zen_task_api.modules.users.Domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.DataConflictException;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.mappers.UserMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service.AuthenticationFailedException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service.TokenService;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service.UserServiceImpl;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private TokenService tokenService;

  @InjectMocks
  private UserServiceImpl userService;

  private RegisterDTO registerDTO;
  private UserModel userModel;

  @BeforeEach
  void setUp() {
    registerDTO = new RegisterDTO("testuser", "test@test.com", "password123", UserRole.USER);
    userModel = UserFactory.create(
        "testes User",
        "test@gmail.com",
        UUID.randomUUID().toString(),
        UserRole.USER);

  }

  @Test
  @DisplayName("Should create user with valid data")
  void createUser_WithValidData_ShouldSucceed() {
    when(userRepository.existsByEmail(anyString()))
        .thenReturn(false);

    when(userRepository.existsByUsername(anyString()))
        .thenReturn(false);

    when(userMapper.toResultDTO(any(UserModel.class)))
        .thenReturn(new RegistrationResultDTO(
            userModel.getUsername(),
            userModel.getEmail(),
            userModel.getRole(),
            userModel.getId(),
            LocalDateTime.now()));

    // Act (Agir)
    RegistrationResultDTO result = userService.createUser(registerDTO);

    // Assert (Verificar)
    assertNotNull(result);
    assertEquals(userModel.getUsername(), result.username());

    // Verifica se o método save() do repositório foi chamado exatamente uma vez
    verify(userRepository, times(1)).save(any(UserModel.class));
  }

  @Test
  @DisplayName("Should return  DataConflictException if email already exists")
  void createUser_WhenEmailExists_ShouldThrowDataConflictException() {
    when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

    DataConflictException exception = assertThrows(DataConflictException.class, () -> {
      userService.createUser(registerDTO);
    });

    assertEquals("Email já em uso", exception.getMessage());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return DataConflictException if username already exists")
  void createUser_WhenUsernameExists_ShouldThrowDataConflictException() {
    when(userRepository.existsByUsername("testuser")).thenReturn(true);

    DataConflictException exception = assertThrows(DataConflictException.class, () -> {
      userService.createUser(registerDTO);
    });

    assertEquals("Nome de usuário já em uso", exception.getMessage());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should return with token")
  void userAuthentication_WithValidCredentials_ShouldReturnToken() throws Exception {
    // Arrange
    AuthenticationDTO authDTO = new AuthenticationDTO("testuser", "password123");
    String expectedToken = "um.jwt.token.falso";
    Authentication auth = mock(Authentication.class); // Mock do objeto de autenticação do Spring

    when(userRepository.existsByUsername("testuser")).thenReturn(true);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
    when(auth.getPrincipal()).thenReturn(userModel);
    when(tokenService.generateToken(userModel)).thenReturn(expectedToken);

    // Act
    AuthenticationResultDTO result = userService.userAuthentication(authDTO);

    // Assert
    assertNotNull(result);
    assertEquals(expectedToken, result.token());
  }

  @Test
  @DisplayName("Should return ResourceNotFoundException if user already exists")
  void userAuthentication_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
    AuthenticationDTO authDTO = new AuthenticationDTO("nonexistent", "password123");
    when(userRepository.existsByUsername("nonexistent"))
        .thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      userService.userAuthentication(authDTO);
    });

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(authenticationManager, never()).authenticate(any());
  }

  @Test
  @DisplayName("Deve lançar BadRequestException se o username for vazio")
  void userAuthentication_WhenUsernameIsBlank_ShouldThrowBadRequestException() {
    AuthenticationDTO authDTO = new AuthenticationDTO("", "password123");

    assertThrows(InvalidInputException.class, () -> {
      userService.userAuthentication(authDTO);
    });
  }

  @Test
  @DisplayName("Deve lançar AuthenticationFailedException para credenciais inválidas")
  void userAuthentication_WithInvalidCredentials_ShouldThrowAuthenticationFailedException() {
    AuthenticationDTO authDTO = new AuthenticationDTO("testuser", "wrongpassword");
    when(userRepository.existsByUsername("testuser"))
        .thenReturn(true);

    when(authenticationManager.authenticate(any()))
        .thenThrow(new AuthenticationException("Bad credentials") {
        });

    assertThrows(AuthenticationFailedException.class, () -> {
      userService.userAuthentication(authDTO);
    });
  }
}
