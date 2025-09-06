package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.DataConflictException;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegistrationResultDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("User Service Integration Tests")
class UserServiceIntegrationTest {

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test - @Transactional will rollback changes
    }

    @Test
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUserSuccessfully() {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
            "integration_user",
            "integration@test.com", 
            "securePassword123",
            UserRole.USER
        );

        // When
        RegistrationResultDTO result = userService.createUser(registerDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("integration_user");
        assertThat(result.email()).isEqualTo("integration@test.com");
        assertThat(result.role()).isEqualTo(UserRole.USER);
        assertThat(result.userId()).isNotNull();
        assertThat(result.createdAt()).isNotNull();

        // Verify user is actually saved in the database
        assertThat(userRepository.existsByUsername("integration_user")).isTrue();
        assertThat(userRepository.existsByEmail("integration@test.com")).isTrue();
    }

    @Test
    @DisplayName("Should authenticate user successfully with valid credentials")
    void shouldAuthenticateUserSuccessfullyWithValidCredentials() throws Exception {
        // Given - First register a user
        RegisterDTO registerDTO = new RegisterDTO(
            "auth_test_user",
            "auth@test.com",
            "testPassword123",
            UserRole.USER
        );
        userService.createUser(registerDTO);

        AuthenticationDTO authDTO = new AuthenticationDTO(
            "auth_test_user",
            "testPassword123"
        );

        // When
        AuthenticationResultDTO result = userService.userAuthentication(authDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.token()).isNotNull();
        assertThat(result.token()).isNotEmpty();
        // Token should be a JWT-like string
        assertThat(result.token()).contains(".");
    }

    @Test
    @DisplayName("Should throw DataConflictException when registering with existing email")
    void shouldThrowDataConflictExceptionWhenRegisteringWithExistingEmail() {
        // Given - Register first user
        RegisterDTO firstUser = new RegisterDTO(
            "first_user",
            "duplicate@test.com",
            "password123",
            UserRole.USER
        );
        userService.createUser(firstUser);

        // Attempt to register second user with same email
        RegisterDTO secondUser = new RegisterDTO(
            "second_user",
            "duplicate@test.com", // Same email
            "password456",
            UserRole.USER
        );

        // When & Then
        assertThatThrownBy(() -> userService.createUser(secondUser))
            .isInstanceOf(DataConflictException.class)
            .hasMessage("Email já em uso");

        // Verify second user was not created
        assertThat(userRepository.existsByUsername("second_user")).isFalse();
    }

    @Test
    @DisplayName("Should throw DataConflictException when registering with existing username")
    void shouldThrowDataConflictExceptionWhenRegisteringWithExistingUsername() {
        // Given - Register first user
        RegisterDTO firstUser = new RegisterDTO(
            "duplicate_username",
            "first@test.com",
            "password123",
            UserRole.USER
        );
        userService.createUser(firstUser);

        // Attempt to register second user with same username
        RegisterDTO secondUser = new RegisterDTO(
            "duplicate_username", // Same username
            "second@test.com",
            "password456",
            UserRole.USER
        );

        // When & Then
        assertThatThrownBy(() -> userService.createUser(secondUser))
            .isInstanceOf(DataConflictException.class)
            .hasMessage("Nome de usuário já em uso");

        // Verify second user was not created
        assertThat(userRepository.existsByEmail("second@test.com")).isFalse();
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when authenticating non-existing user")
    void shouldThrowResourceNotFoundExceptionWhenAuthenticatingNonExistingUser() {
        // Given
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "non_existing_user",
            "password123"
        );

        // When & Then
        assertThatThrownBy(() -> userService.userAuthentication(authDTO))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Should throw BadRequestException when username is blank during authentication")
    void shouldThrowBadRequestExceptionWhenUsernameIsBlankDuringAuthentication() {
        // Given
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "", // Blank username
            "password123"
        );

        // When & Then
        assertThatThrownBy(() -> userService.userAuthentication(authDTO))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Should throw AuthenticationFailedException with wrong password")
    void shouldThrowAuthenticationFailedExceptionWithWrongPassword() {
        // Given - Register a user
        RegisterDTO registerDTO = new RegisterDTO(
            "password_test_user",
            "password@test.com",
            "correctPassword",
            UserRole.USER
        );
        userService.createUser(registerDTO);

        // Try to authenticate with wrong password
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "password_test_user",
            "wrongPassword"
        );

        // When & Then
        assertThatThrownBy(() -> userService.userAuthentication(authDTO))
            .isInstanceOf(AuthenticationFailedException.class);
    }

    @Test
    @DisplayName("Should register users with USER role (factory limitation)")
    void shouldRegisterUsersWithUserRole() {
        // Given
        RegisterDTO userDTO = new RegisterDTO(
            "regular_user",
            "user@test.com",
            "password123",
            UserRole.USER
        );

        RegisterDTO adminDTO = new RegisterDTO(
            "admin_user",
            "admin@test.com",
            "password123",
            UserRole.ADMIN
        );

        // When
        RegistrationResultDTO userResult = userService.createUser(userDTO);
        RegistrationResultDTO adminResult = userService.createUser(adminDTO);

        // Then - Note: Due to UserFactory implementation, all users are created as USER role
        assertThat(userResult.role()).isEqualTo(UserRole.USER);
        assertThat(adminResult.role()).isEqualTo(UserRole.USER); // Factory always creates USER

        // Verify both users are saved
        UserDetails userDetails = userRepository.findByUsername("regular_user").orElseThrow();
        UserDetails adminDetails = userRepository.findByUsername("admin_user").orElseThrow();
        
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(adminDetails.getAuthorities()).hasSize(1);
    }

    @Test
    @DisplayName("Should handle multiple user registrations in sequence")
    void shouldHandleMultipleUserRegistrationsInSequence() {
        // Given
        RegisterDTO user1DTO = new RegisterDTO("user1", "user1@test.com", "password123", UserRole.USER);
        RegisterDTO user2DTO = new RegisterDTO("user2", "user2@test.com", "password123", UserRole.USER);
        RegisterDTO user3DTO = new RegisterDTO("user3", "user3@test.com", "password123", UserRole.ADMIN);

        // When
        RegistrationResultDTO result1 = userService.createUser(user1DTO);
        RegistrationResultDTO result2 = userService.createUser(user2DTO);
        RegistrationResultDTO result3 = userService.createUser(user3DTO);

        // Then
        assertThat(result1.userId()).isNotEqualTo(result2.userId());
        assertThat(result2.userId()).isNotEqualTo(result3.userId());
        assertThat(result1.userId()).isNotEqualTo(result3.userId());

        // Verify all users exist in repository
        assertThat(userRepository.existsByUsername("user1")).isTrue();
        assertThat(userRepository.existsByUsername("user2")).isTrue();
        assertThat(userRepository.existsByUsername("user3")).isTrue();
        
        assertThat(userRepository.existsByEmail("user1@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("user2@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("user3@test.com")).isTrue();
    }

    @Test
    @DisplayName("Should complete full user registration and authentication flow")
    void shouldCompleteFullUserRegistrationAndAuthenticationFlow() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
            "flow_test_user",
            "flow@test.com",
            "flowTestPassword123",
            UserRole.USER
        );

        // When - Registration
        RegistrationResultDTO registrationResult = userService.createUser(registerDTO);

        // Then - Verify registration
        assertThat(registrationResult).isNotNull();
        assertThat(registrationResult.username()).isEqualTo("flow_test_user");

        // When - Authentication
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "flow_test_user",
            "flowTestPassword123"
        );
        AuthenticationResultDTO authResult = userService.userAuthentication(authDTO);

        // Then - Verify authentication
        assertThat(authResult).isNotNull();
        assertThat(authResult.token()).isNotNull();
        assertThat(authResult.token()).isNotEmpty();

        // Verify user exists in repository
        assertThat(userRepository.existsByUsername("flow_test_user")).isTrue();
        assertThat(userRepository.existsByEmail("flow@test.com")).isTrue();
    }
}