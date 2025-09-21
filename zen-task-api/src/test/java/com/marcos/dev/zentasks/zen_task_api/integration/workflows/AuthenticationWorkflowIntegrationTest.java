package com.marcos.dev.zentasks.zen_task_api.integration.workflows;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Authentication Workflow Integration Tests")
class AuthenticationWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean setup for each test
    }

    @Test
    @DisplayName("Should complete full user registration and authentication workflow")
    void shouldCompleteFullUserRegistrationAndAuthenticationWorkflow() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO(
                "workflow_user",
                "workflow@example.com",
                "workflowPassword123");

        MvcResult registrationResult = mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("workflow_user"))
                .andExpect(jsonPath("$.email").value("workflow@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andReturn();

        assertThat(userRepository.existsByUsername("workflow_user")).isTrue();
        assertThat(userRepository.existsByEmail("workflow@example.com")).isTrue();

        String registrationResponseBody = registrationResult.getResponse().getContentAsString();
        JsonNode registrationJson = objectMapper.readTree(registrationResponseBody);
        String userId = registrationJson.get("userId").asText();
        assertThat(userId).isNotNull();

        AuthenticationDTO authDTO = new AuthenticationDTO(
                "workflow_user",
                "workflowPassword123");

        MvcResult authResult = mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String authResponseBody = authResult.getResponse().getContentAsString();
        JsonNode authJson = objectMapper.readTree(authResponseBody);
        String token = authJson.get("token").asText();

        assertThat(token).contains(".");
        String[] tokenParts = token.split("\\.");
        assertThat(tokenParts).hasSize(3);

        assertThat(userRepository.existsByUsername("workflow_user")).isTrue();
        assertThat(userRepository.existsByEmail("workflow@example.com")).isTrue();
    }

    @Test
    @DisplayName("Should handle multiple user registrations and authentications")
    void shouldHandleMultipleUserRegistrationsAndAuthentications() throws Exception {
        // User 1: Regular user
        RegisterDTO user1DTO = new RegisterDTO(
                "user1_workflow",
                "user1@example.com",
                "password123");

        // User 2: Another regular user
        RegisterDTO user2DTO = new RegisterDTO(
                "user2_workflow",
                "user2@example.com",
                "password456");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1_workflow"));

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user2_workflow"));

        assertThat(userRepository.existsByUsername("user1_workflow")).isTrue();
        assertThat(userRepository.existsByUsername("user2_workflow")).isTrue();

        AuthenticationDTO auth1DTO = new AuthenticationDTO("user1_workflow", "password123");
        AuthenticationDTO auth2DTO = new AuthenticationDTO("user2_workflow", "password456");

        MvcResult auth1Result = mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth1DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        MvcResult auth2Result = mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth2DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        // Verify different users get different tokens
        String token1 = extractTokenFromResponse(auth1Result);
        String token2 = extractTokenFromResponse(auth2Result);
        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Should prevent authentication with wrong credentials")
    void shouldPreventAuthenticationWithWrongCredentials() throws Exception {
        // Step 1: Register a user
        RegisterDTO registerDTO = new RegisterDTO(
                "secure_user",
                "secure@example.com",
                "correctPassword123");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk());

        AuthenticationDTO wrongPasswordAuth = new AuthenticationDTO(
                "secure_user",
                "wrongPassword");

        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPasswordAuth)))
                .andExpect(status().isForbidden()); // Service returns 403 for auth failures

        AuthenticationDTO wrongUsernameAuth = new AuthenticationDTO(
                "wrong_user",
                "correctPassword123");

        mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongUsernameAuth)))
                .andExpect(status().isNotFound()); // User not found
    }

    @Test
    @DisplayName("Should prevent duplicate user registration")
    void shouldPreventDuplicateUserRegistration() throws Exception {
        // Step 1: Register first user
        RegisterDTO firstUser = new RegisterDTO(
                "duplicate_test",
                "duplicate@example.com",
                "password123"
                );

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("duplicate_test"));

        RegisterDTO duplicateUsername = new RegisterDTO(
                "duplicate_test", // Same username
                "different@example.com",
                "password456");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUsername)))
                .andExpect(status().isConflict());

        RegisterDTO duplicateEmail = new RegisterDTO(
                "different_user",
                "duplicate@example.com", // Same email
                "password789");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateEmail)))
                .andExpect(status().isConflict());

        assertThat(userRepository.existsByUsername("duplicate_test")).isTrue();
        assertThat(userRepository.existsByUsername("different_user")).isFalse();
        assertThat(userRepository.existsByEmail("duplicate@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("different@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should validate user input during registration")
    void shouldValidateUserInputDuringRegistration() throws Exception {
        RegisterDTO shortUsername = new RegisterDTO(
                "ab", // Too short (< 3 characters)
                "test@example.com",
                "password123");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortUsername)))
                .andExpect(status().isBadRequest());

        RegisterDTO invalidEmail = new RegisterDTO(
                "valid_user",
                "invalid-email", // Invalid email format
                "password123"
                );

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmail)))
                .andExpect(status().isBadRequest());

        // Test 3: Short password
        RegisterDTO shortPassword = new RegisterDTO(
                "valid_user",
                "valid@example.com",
                "123");

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shortPassword)))
                .andExpect(status().isBadRequest());

        assertThat(userRepository.existsByUsername("ab")).isFalse();
        assertThat(userRepository.existsByUsername("valid_user")).isFalse();
        assertThat(userRepository.existsByEmail("invalid-email")).isFalse();
        assertThat(userRepository.existsByEmail("valid@example.com")).isFalse();
    }

    private String extractTokenFromResponse(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        return json.get("token").asText();
    }
}