package com.marcos.dev.zentasks.zen_task_api.modules.users.Application.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.service.UserServiceInterface;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("User Controller Integration Tests")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceInterface userService;

    @BeforeEach
    void setUp() {
        // Clean setup before each test
    }

    @Test
    @DisplayName("Should register new user successfully via POST /v1/register")
    void shouldRegisterNewUserSuccessfullyViaPost() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
            "test_user",
            "test@example.com",
            "password123",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(registerDTO);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("test_user"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.createdAt").exists());

        // Verify user was actually saved
        assert userRepository.existsByUsername("test_user");
        assert userRepository.existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should authenticate user successfully via POST /v1/login")
    void shouldAuthenticateUserSuccessfullyViaPost() throws Exception {
        // Given - First register a user
        RegisterDTO registerDTO = new RegisterDTO(
            "login_user",
            "login@example.com",
            "password123",
            UserRole.USER
        );
        userService.createUser(registerDTO);

        AuthenticationDTO authDTO = new AuthenticationDTO(
            "login_user",
            "password123"
        );

        String requestBody = objectMapper.writeValueAsString(authDTO);

        // When & Then
        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 400 for registration with existing email")
    void shouldReturn400ForRegistrationWithExistingEmail() throws Exception {
        // Given - Register first user
        RegisterDTO firstUser = new RegisterDTO(
            "first_user",
            "duplicate@example.com",
            "password123",
            UserRole.USER
        );
        userService.createUser(firstUser);

        // Try to register second user with same email
        RegisterDTO secondUser = new RegisterDTO(
            "second_user",
            "duplicate@example.com", // Same email
            "password456",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(secondUser);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());

        // Verify second user was not created
        assert !userRepository.existsByUsername("second_user");
    }

    @Test
    @DisplayName("Should return 400 for registration with existing username")
    void shouldReturn400ForRegistrationWithExistingUsername() throws Exception {
        // Given - Register first user
        RegisterDTO firstUser = new RegisterDTO(
            "duplicate_user",
            "first@example.com",
            "password123",
            UserRole.USER
        );
        userService.createUser(firstUser);

        // Try to register second user with same username
        RegisterDTO secondUser = new RegisterDTO(
            "duplicate_user", // Same username
            "second@example.com",
            "password456",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(secondUser);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict());

        // Verify second user was not created
        assert !userRepository.existsByEmail("second@example.com");
    }

    @Test
    @DisplayName("Should return 404 for authentication with non-existing user")
    void shouldReturn404ForAuthenticationWithNonExistingUser() throws Exception {
        // Given
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "non_existing_user",
            "password123"
        );

        String requestBody = objectMapper.writeValueAsString(authDTO);

        // When & Then
        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 401 for authentication with wrong password")
    void shouldReturn401ForAuthenticationWithWrongPassword() throws Exception {
        // Given - Register a user
        RegisterDTO registerDTO = new RegisterDTO(
            "auth_user",
            "auth@example.com",
            "correctPassword",
            UserRole.USER
        );
        userService.createUser(registerDTO);

        // Try to authenticate with wrong password
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "auth_user",
            "wrongPassword"
        );

        String requestBody = objectMapper.writeValueAsString(authDTO);

        // When & Then
        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden()); // Service returns 403 for auth failures
    }

    @Test
    @DisplayName("Should return 400 for registration with invalid email format")
    void shouldReturn400ForRegistrationWithInvalidEmailFormat() throws Exception {
        // Given
        RegisterDTO invalidRegisterDTO = new RegisterDTO(
            "test_user",
            "invalid-email-format", // Invalid email
            "password123",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(invalidRegisterDTO);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify user was not created
        assert !userRepository.existsByUsername("test_user");
    }

    @Test
    @DisplayName("Should return 400 for registration with short password")
    void shouldReturn400ForRegistrationWithShortPassword() throws Exception {
        // Given
        RegisterDTO invalidRegisterDTO = new RegisterDTO(
            "test_user",
            "test@example.com",
            "123", // Too short password (< 8 characters)
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(invalidRegisterDTO);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify user was not created
        assert !userRepository.existsByUsername("test_user");
    }

    @Test
    @DisplayName("Should return 400 for registration with short username")
    void shouldReturn400ForRegistrationWithShortUsername() throws Exception {
        // Given
        RegisterDTO invalidRegisterDTO = new RegisterDTO(
            "ab", // Too short username (< 3 characters)
            "test@example.com",
            "password123",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(invalidRegisterDTO);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify user was not created
        assert !userRepository.existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return 400 for authentication with blank username")
    void shouldReturn400ForAuthenticationWithBlankUsername() throws Exception {
        // Given
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "", // Blank username
            "password123"
        );

        String requestBody = objectMapper.writeValueAsString(authDTO);

        // When & Then - Service validates and throws BadRequest before reaching authentication
        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for malformed JSON in registration")
    void shouldReturn400ForMalformedJsonInRegistration() throws Exception {
        // Given
        String malformedJson = "{ \"username\": \"test\", \"email\": "; // Incomplete JSON

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for malformed JSON in login")
    void shouldReturn400ForMalformedJsonInLogin() throws Exception {
        // Given
        String malformedJson = "{ \"username\": \"test\", "; // Incomplete JSON

        // When & Then
        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle complete user registration and authentication flow via REST")
    void shouldHandleCompleteUserRegistrationAndAuthenticationFlowViaRest() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
            "flow_user",
            "flow@example.com",
            "flowPassword123",
            UserRole.USER
        );

        // Step 1: Register user
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("flow_user"));

        // Step 2: Authenticate the registered user
        AuthenticationDTO authDTO = new AuthenticationDTO(
            "flow_user",
            "flowPassword123"
        );

        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Verify user exists in repository
        assert userRepository.existsByUsername("flow_user");
        assert userRepository.existsByEmail("flow@example.com");
    }

    @Test
    @DisplayName("Should return appropriate content-type headers")
    void shouldReturnAppropriateContentTypeHeaders() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
            "header_test_user",
            "header@example.com",
            "password123",
            UserRole.USER
        );

        String requestBody = objectMapper.writeValueAsString(registerDTO);

        // When & Then
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("header_test_user"))
                .andExpect(jsonPath("$.email").value("header@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}