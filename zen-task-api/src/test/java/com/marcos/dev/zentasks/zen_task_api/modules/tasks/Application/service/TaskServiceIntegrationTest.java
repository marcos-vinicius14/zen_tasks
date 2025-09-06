package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Task Service Integration Tests")
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserModel testUser;

    @BeforeEach
    void setUp() {
        // Create and save test user
        testUser = UserFactory.create(
            "Integration Test User",
            "integration@test.com",
            "password123",
            UserRole.USER
        );
        testUser = userRepository.save(testUser);

        // Set up security context for authentication
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("Should create task successfully with real repository")
    void shouldCreateTaskSuccessfullyWithRealRepository() {
        // Given
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
            "Integration Test Task",
            "Testing task creation with real database",
            LocalDate.now().plusDays(5),
            true,
            true
        );

        // When
        TaskResponseDTO response = taskService.createNewTask(createTaskDTO);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("Integration Test Task");
        assertThat(response.description()).isEqualTo("Testing task creation with real database");
        assertThat(response.taskStatus()).isEqualTo(TaskStatus.CREATED);

        // Verify task was actually saved in the database
        assertThat(taskRepository.findById(response.id())).isPresent();
        assertThat(taskRepository.countIncompleteTasksByUser(testUser)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should create multiple tasks for the same user")
    void shouldCreateMultipleTasksForSameUser() {
        // Given
        CreateTaskDTO task1DTO = new CreateTaskDTO(
            "First Task",
            "First task description",
            LocalDate.now().plusDays(1),
            true,
            false
        );

        CreateTaskDTO task2DTO = new CreateTaskDTO(
            "Second Task", 
            "Second task description",
            LocalDate.now().plusDays(3),
            false,
            true
        );

        CreateTaskDTO task3DTO = new CreateTaskDTO(
            "Third Task",
            "Third task description",
            LocalDate.now().plusDays(7),
            false,
            false
        );

        // When
        TaskResponseDTO response1 = taskService.createNewTask(task1DTO);
        TaskResponseDTO response2 = taskService.createNewTask(task2DTO);
        TaskResponseDTO response3 = taskService.createNewTask(task3DTO);

        // Then
        assertThat(response1.id()).isNotNull();
        assertThat(response2.id()).isNotNull();
        assertThat(response3.id()).isNotNull();

        // All should have different IDs
        assertThat(response1.id()).isNotEqualTo(response2.id());
        assertThat(response2.id()).isNotEqualTo(response3.id());
        assertThat(response1.id()).isNotEqualTo(response3.id());

        // Verify all tasks are saved and associated with the user
        assertThat(taskRepository.findByUser(testUser)).hasSize(3);
        assertThat(taskRepository.countIncompleteTasksByUser(testUser)).isEqualTo(3);
    }

    @Test
    @DisplayName("Should throw exception when user is not authenticated")
    void shouldThrowExceptionWhenUserNotAuthenticated() {
        // Given
        SecurityContextHolder.getContext().setAuthentication(null); // Remove authentication

        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
            "Unauthorized Task",
            "This should fail",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        // When & Then
        assertThatThrownBy(() -> taskService.createNewTask(createTaskDTO))
            .isInstanceOf(ForbiddenAccessException.class)
            .hasMessage("Usuário não autenticado");

        // Verify no task was created
        assertThat(taskRepository.findByUser(testUser)).isEmpty();
    }

    @Test
    @DisplayName("Should persist task with correct quadrant based on urgency and importance")
    void shouldPersistTaskWithCorrectQuadrantBasedOnUrgencyAndImportance() {
        // Given - Create tasks for all quadrants
        CreateTaskDTO doNowTaskDTO = new CreateTaskDTO(
            "Do Now Task", 
            "Urgent and Important",
            LocalDate.now().plusDays(1),
            true,  // urgent
            true   // important
        );

        CreateTaskDTO scheduleTaskDTO = new CreateTaskDTO(
            "Schedule Task",
            "Important but not urgent", 
            LocalDate.now().plusDays(5),
            false, // not urgent
            true   // important
        );

        CreateTaskDTO delegateTaskDTO = new CreateTaskDTO(
            "Delegate Task",
            "Urgent but not important",
            LocalDate.now().plusDays(1),
            true,  // urgent
            false  // not important
        );

        CreateTaskDTO eliminateTaskDTO = new CreateTaskDTO(
            "Eliminate Task",
            "Neither urgent nor important",
            LocalDate.now().plusDays(10),
            false, // not urgent
            false  // not important
        );

        // When
        TaskResponseDTO doNowResponse = taskService.createNewTask(doNowTaskDTO);
        TaskResponseDTO scheduleResponse = taskService.createNewTask(scheduleTaskDTO);
        TaskResponseDTO delegateResponse = taskService.createNewTask(delegateTaskDTO);
        TaskResponseDTO eliminateResponse = taskService.createNewTask(eliminateTaskDTO);

        // Then - Verify tasks are saved with correct properties
        var doNowTask = taskRepository.findById(doNowResponse.id()).orElseThrow();
        var scheduleTask = taskRepository.findById(scheduleResponse.id()).orElseThrow();
        var delegateTask = taskRepository.findById(delegateResponse.id()).orElseThrow();
        var eliminateTask = taskRepository.findById(eliminateResponse.id()).orElseThrow();

        assertThat(doNowTask.getQuadrant().name()).isEqualTo("DO_NOW");
        assertThat(scheduleTask.getQuadrant().name()).isEqualTo("SCHEDULE");
        assertThat(delegateTask.getQuadrant().name()).isEqualTo("DELEGATE");
        assertThat(eliminateTask.getQuadrant().name()).isEqualTo("ELIMINATE");

        // Verify repository queries work correctly
        assertThat(taskRepository.findByUserAndQuadrant(testUser, doNowTask.getQuadrant())).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, scheduleTask.getQuadrant())).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, delegateTask.getQuadrant())).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, eliminateTask.getQuadrant())).hasSize(1);
    }

    @Test
    @DisplayName("Should handle task creation with edge case dates")
    void shouldHandleTaskCreationWithEdgeCaseDates() {
        // Given
        CreateTaskDTO taskTomorrowDTO = new CreateTaskDTO(
            "Tomorrow Task",
            "Due tomorrow",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        CreateTaskDTO taskFarFutureDTO = new CreateTaskDTO(
            "Far Future Task", 
            "Due in far future",
            LocalDate.now().plusYears(1),
            false,
            true
        );

        // When
        TaskResponseDTO tomorrowResponse = taskService.createNewTask(taskTomorrowDTO);
        TaskResponseDTO farFutureResponse = taskService.createNewTask(taskFarFutureDTO);

        // Then
        var tomorrowTask = taskRepository.findById(tomorrowResponse.id()).orElseThrow();
        var farFutureTask = taskRepository.findById(farFutureResponse.id()).orElseThrow();

        assertThat(tomorrowTask.getDueDate()).isEqualTo(LocalDate.now().plusDays(1));
        assertThat(farFutureTask.getDueDate()).isEqualTo(LocalDate.now().plusYears(1));
        
        assertThat(tomorrowTask.getStatus()).isEqualTo(TaskStatus.CREATED);
        assertThat(farFutureTask.getStatus()).isEqualTo(TaskStatus.CREATED);
        
        assertThat(tomorrowTask.isCompleted()).isFalse();
        assertThat(farFutureTask.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("Should handle concurrent task creation for the same user")
    void shouldHandleConcurrentTaskCreationForSameUser() {
        // Given
        CreateTaskDTO task1DTO = new CreateTaskDTO(
            "Concurrent Task 1",
            "First concurrent task",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        CreateTaskDTO task2DTO = new CreateTaskDTO(
            "Concurrent Task 2",
            "Second concurrent task", 
            LocalDate.now().plusDays(2),
            false,
            true
        );

        // When - Simulate rapid task creation
        TaskResponseDTO response1 = taskService.createNewTask(task1DTO);
        TaskResponseDTO response2 = taskService.createNewTask(task2DTO);

        // Then
        assertThat(response1.id()).isNotEqualTo(response2.id());
        
        var savedTasks = taskRepository.findByUser(testUser);
        assertThat(savedTasks).hasSize(2);
        
        var titles = savedTasks.stream().map(task -> task.getTitle()).toList();
        assertThat(titles).containsExactlyInAnyOrder("Concurrent Task 1", "Concurrent Task 2");
    }
}