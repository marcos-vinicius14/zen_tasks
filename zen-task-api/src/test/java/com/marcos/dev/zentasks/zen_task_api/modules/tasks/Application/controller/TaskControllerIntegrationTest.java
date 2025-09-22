package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Task Controller Integration Tests")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private UserModel testUser;

    @BeforeEach
    void setUp() {
        // Create and save test user
        testUser = UserFactory.create(
            "Controller Test User",
            "controller@test.com",
            "password123"
        );
        testUser = userRepository.save(testUser);

        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Should create task successfully via POST /v1/tasks")
    void shouldCreateTaskSuccessfullyViaPost() throws Exception {
        // Given
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
            "Integration Test Task",
            "Testing task creation via REST API",
            LocalDate.now().plusDays(3),
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(createTaskDTO);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("Testing task creation via REST API"))
                .andExpect(jsonPath("$.taskStatus").value("CREATED"));

        // Verify task was actually saved
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.size() == 1;
        assert savedTasks.get(0).getTitle().equals("Integration Test Task");
    }

    @Test
    @DisplayName("Should return 400 for invalid task data")
    void shouldReturn400ForInvalidTaskData() throws Exception {
        // Given - Invalid task with null title
        CreateTaskDTO invalidTaskDTO = new CreateTaskDTO(
            null, // Invalid: null title
            "Description",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(invalidTaskDTO);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify no task was created
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.isEmpty();
    }

    @Test
    @DisplayName("Should return 400 for blank title")
    void shouldReturn400ForBlankTitle() throws Exception {
        // Given
        CreateTaskDTO invalidTaskDTO = new CreateTaskDTO(
            "   ", // Invalid: blank title
            "Description",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(invalidTaskDTO);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify no task was created
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.isEmpty();
    }

    @Test
    @DisplayName("Should return 400 for past due date")
    void shouldReturn400ForPastDueDate() throws Exception {
        // Given
        CreateTaskDTO invalidTaskDTO = new CreateTaskDTO(
            "Valid Title",
            "Valid Description",
            LocalDate.now().minusDays(1), // Invalid: past date
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(invalidTaskDTO);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        // Verify no task was created
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.isEmpty();
    }

    @Test
    @DisplayName("Should return 403 when user is not authenticated")
    void shouldReturn403WhenUserNotAuthenticated() throws Exception {
        // Given
        SecurityContextHolder.getContext().setAuthentication(null); // Remove authentication

        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
            "Unauthorized Task",
            "This should fail",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(createTaskDTO);

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());

        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.isEmpty();
    }

    @Test
    @DisplayName("Should return 400 for malformed JSON")
    void shouldReturn400ForMalformedJson() throws Exception {
        String malformedJson = "{ \"title\": \"Test\", \"description\": "; // Incomplete JSON

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());

        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.isEmpty();
    }

    @Test
    @DisplayName("Should handle tasks with different quadrants")
    void shouldHandleTasksWithDifferentQuadrants() throws Exception {
        // Given - Tasks for all quadrants
        CreateTaskDTO doNowTask = new CreateTaskDTO(
            "Do Now Task",
            "Urgent and Important",
            LocalDate.now().plusDays(1),
            true,  // urgent
            true   // important
        );

        CreateTaskDTO scheduleTask = new CreateTaskDTO(
            "Schedule Task",
            "Important but not urgent",
            LocalDate.now().plusDays(5),
            false, // not urgent
            true   // important
        );

        CreateTaskDTO delegateTask = new CreateTaskDTO(
            "Delegate Task",
            "Urgent but not important",
            LocalDate.now().plusDays(1),
            true,  // urgent
            false  // not important
        );

        CreateTaskDTO eliminateTask = new CreateTaskDTO(
            "Eliminate Task",
            "Neither urgent nor important",
            LocalDate.now().plusDays(10),
            false, // not urgent
            false  // not important
        );

        // When & Then - Create all tasks
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doNowTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Do Now Task"));

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scheduleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Schedule Task"));

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(delegateTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Delegate Task"));

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eliminateTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Eliminate Task"));

        // Verify all tasks were created
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.size() == 4;
    }

    @Test
    @DisplayName("Should handle edge case dates correctly")
    void shouldHandleEdgeCaseDatesCorrectly() throws Exception {
        // Given - Task due tomorrow
        CreateTaskDTO tomorrowTask = new CreateTaskDTO(
            "Tomorrow Task",
            "Due tomorrow",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        // Given - Task due far in the future
        CreateTaskDTO farFutureTask = new CreateTaskDTO(
            "Far Future Task",
            "Due in one year",
            LocalDate.now().plusYears(1),
            false,
            true
        );

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tomorrowTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tomorrow Task"));

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(farFutureTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Far Future Task"));

        // Verify tasks were created
        var savedTasks = taskRepository.findByUser(testUser);
        assert savedTasks.size() == 2;
    }

    @Test
    @DisplayName("Should return appropriate content-type headers")
    void shouldReturnAppropriateContentTypeHeaders() throws Exception {
        // Given
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
            "Header Test Task",
            "Testing headers",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        String requestBody = objectMapper.writeValueAsString(createTaskDTO);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Header Test Task"))
                .andExpect(jsonPath("$.description").value("Testing headers"))
                .andExpect(jsonPath("$.taskStatus").value("CREATED"));
    }
}