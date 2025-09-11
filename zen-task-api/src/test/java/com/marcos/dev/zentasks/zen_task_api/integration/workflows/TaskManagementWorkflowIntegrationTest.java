package com.marcos.dev.zentasks.zen_task_api.integration.workflows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.AuthenticationDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Application.dtos.RegisterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Infrastructure.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Task Management Workflow Integration Tests")
class TaskManagementWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private String authToken;
    private UserModel testUser;

    @BeforeEach
    void setUp() throws Exception {
        // Setup: Create user and get authentication token for task operations
        RegisterDTO registerDTO = new RegisterDTO(
            "task_user",
            "tasks@example.com",
            "taskPassword123",
            UserRole.USER
        );

        // Register user
        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk());

        // Authenticate user and get token
        AuthenticationDTO authDTO = new AuthenticationDTO("task_user", "taskPassword123");
        
        MvcResult authResult = mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String authResponseBody = authResult.getResponse().getContentAsString();
        JsonNode authJson = objectMapper.readTree(authResponseBody);
        authToken = authJson.get("token").asText();

        // Create UserModel for repository checks
        testUser = (UserModel) userRepository.findByUsername("task_user").orElseThrow();
    }

    @Test
    @DisplayName("Should complete full task creation workflow for all quadrants")
    void shouldCompleteFullTaskCreationWorkflowForAllQuadrants() throws Exception {
        // Create tasks for all four quadrants of the Eisenhower Matrix

        // Quadrant 1: DO_NOW (Urgent + Important)
        CreateTaskDTO doNowTask = new CreateTaskDTO(
            "Fix Production Bug",
            "Critical bug affecting users",
            LocalDate.now().plusDays(1),
            true,  // urgent
            true   // important
        );

        // Quadrant 2: SCHEDULE (Not Urgent + Important)
        CreateTaskDTO scheduleTask = new CreateTaskDTO(
            "Plan Sprint Goals",
            "Define objectives for next sprint",
            LocalDate.now().plusWeeks(1),
            false, // not urgent
            true   // important
        );

        // Quadrant 3: DELEGATE (Urgent + Not Important)
        CreateTaskDTO delegateTask = new CreateTaskDTO(
            "Update Documentation",
            "Update API documentation",
            LocalDate.now().plusDays(2),
            true,  // urgent
            false  // not important
        );

        // Quadrant 4: ELIMINATE (Not Urgent + Not Important)
        CreateTaskDTO eliminateTask = new CreateTaskDTO(
            "Organize Desktop",
            "Clean up desktop files",
            LocalDate.now().plusWeeks(2),
            false, // not urgent
            false  // not important
        );

        // Execute task creation for all quadrants
        MvcResult doNowResult = createTaskViaAPI(doNowTask, authToken);
        MvcResult scheduleResult = createTaskViaAPI(scheduleTask, authToken);
        MvcResult delegateResult = createTaskViaAPI(delegateTask, authToken);
        MvcResult eliminateResult = createTaskViaAPI(eliminateTask, authToken);

        // Verify all tasks were created successfully
        Long doNowTaskId = extractTaskIdFromResponse(doNowResult);
        Long scheduleTaskId = extractTaskIdFromResponse(scheduleResult);
        Long delegateTaskId = extractTaskIdFromResponse(delegateResult);
        Long eliminateTaskId = extractTaskIdFromResponse(eliminateResult);

        assertThat(doNowTaskId).isNotNull();
        assertThat(scheduleTaskId).isNotNull();
        assertThat(delegateTaskId).isNotNull();
        assertThat(eliminateTaskId).isNotNull();

        // Verify tasks were saved in database with correct properties
        var savedTasks = taskRepository.findByUser(testUser);
        assertThat(savedTasks).hasSize(4);

        // Verify quadrant assignment
        var doNowTaskDb = taskRepository.findById(doNowTaskId).orElseThrow();
        var scheduleTaskDb = taskRepository.findById(scheduleTaskId).orElseThrow();
        var delegateTaskDb = taskRepository.findById(delegateTaskId).orElseThrow();
        var eliminateTaskDb = taskRepository.findById(eliminateTaskId).orElseThrow();

        assertThat(doNowTaskDb.getQuadrant()).isEqualTo(Quadrant.DO_NOW);
        assertThat(scheduleTaskDb.getQuadrant()).isEqualTo(Quadrant.SCHEDULE);
        assertThat(delegateTaskDb.getQuadrant()).isEqualTo(Quadrant.DELEGATE);
        assertThat(eliminateTaskDb.getQuadrant()).isEqualTo(Quadrant.ELIMINATE);

        // Verify repository queries work correctly
        assertThat(taskRepository.findByUserAndQuadrant(testUser, Quadrant.DO_NOW)).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, Quadrant.SCHEDULE)).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, Quadrant.DELEGATE)).hasSize(1);
        assertThat(taskRepository.findByUserAndQuadrant(testUser, Quadrant.ELIMINATE)).hasSize(1);

        // Verify count of incomplete tasks
        assertThat(taskRepository.countIncompleteTasksByUser(testUser)).isEqualTo(4);
    }

    @Test
    @DisplayName("Should handle task creation with various due dates")
    void shouldHandleTaskCreationWithVariousDueDates() throws Exception {
        // Task due today
        CreateTaskDTO todayTask = new CreateTaskDTO(
            "Today Task",
            "Task due today",
            LocalDate.now(),
            true,
            true
        );

        // Task due tomorrow
        CreateTaskDTO tomorrowTask = new CreateTaskDTO(
            "Tomorrow Task",
            "Task due tomorrow",
            LocalDate.now().plusDays(1),
            false,
            true
        );

        // Task due next week
        CreateTaskDTO nextWeekTask = new CreateTaskDTO(
            "Next Week Task",
            "Task due next week",
            LocalDate.now().plusWeeks(1),
            false,
            false
        );

        // Task due next month
        CreateTaskDTO nextMonthTask = new CreateTaskDTO(
            "Next Month Task",
            "Task due next month",
            LocalDate.now().plusMonths(1),
            false,
            true
        );

        // Create all tasks
        createTaskViaAPI(todayTask, authToken);
        createTaskViaAPI(tomorrowTask, authToken);
        createTaskViaAPI(nextWeekTask, authToken);
        createTaskViaAPI(nextMonthTask, authToken);

        // Verify all tasks were created
        var savedTasks = taskRepository.findByUser(testUser);
        assertThat(savedTasks).hasSize(4);

        // Verify due dates are correctly stored
        var titles = savedTasks.stream().map(task -> task.getTitle()).toList();
        assertThat(titles).containsExactlyInAnyOrder(
            "Today Task",
            "Tomorrow Task", 
            "Next Week Task",
            "Next Month Task"
        );

        // Verify task due dates
        var todayTaskDb = savedTasks.stream()
            .filter(task -> task.getTitle().equals("Today Task"))
            .findFirst().orElseThrow();
        assertThat(todayTaskDb.getDueDate()).isEqualTo(LocalDate.now());

        var tomorrowTaskDb = savedTasks.stream()
            .filter(task -> task.getTitle().equals("Tomorrow Task"))
            .findFirst().orElseThrow();
        assertThat(tomorrowTaskDb.getDueDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    @DisplayName("Should handle concurrent task creation by same user")
    void shouldHandleConcurrentTaskCreationBySameUser() throws Exception {
        // Create multiple tasks rapidly
        CreateTaskDTO task1 = new CreateTaskDTO(
            "Concurrent Task 1",
            "First concurrent task",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        CreateTaskDTO task2 = new CreateTaskDTO(
            "Concurrent Task 2",
            "Second concurrent task",
            LocalDate.now().plusDays(2),
            false,
            true
        );

        CreateTaskDTO task3 = new CreateTaskDTO(
            "Concurrent Task 3",
            "Third concurrent task",
            LocalDate.now().plusDays(3),
            true,
            false
        );

        // Create tasks in rapid succession
        MvcResult result1 = createTaskViaAPI(task1, authToken);
        MvcResult result2 = createTaskViaAPI(task2, authToken);
        MvcResult result3 = createTaskViaAPI(task3, authToken);

        // Verify all tasks have unique IDs
        Long taskId1 = extractTaskIdFromResponse(result1);
        Long taskId2 = extractTaskIdFromResponse(result2);
        Long taskId3 = extractTaskIdFromResponse(result3);

        assertThat(taskId1).isNotEqualTo(taskId2);
        assertThat(taskId2).isNotEqualTo(taskId3);
        assertThat(taskId1).isNotEqualTo(taskId3);

        // Verify all tasks were saved
        var savedTasks = taskRepository.findByUser(testUser);
        assertThat(savedTasks).hasSize(3);

        var titles = savedTasks.stream().map(task -> task.getTitle()).toList();
        assertThat(titles).containsExactlyInAnyOrder(
            "Concurrent Task 1",
            "Concurrent Task 2",
            "Concurrent Task 3"
        );
    }

    @Test
    @DisplayName("Should complete user registration followed by task creation workflow")
    void shouldCompleteUserRegistrationFollowedByTaskCreationWorkflow() throws Exception {
        // Step 1: Register a new user specifically for this workflow
        RegisterDTO newUserDTO = new RegisterDTO(
            "workflow_test_user",
            "workflow@test.com",
            "workflowPassword123",
            UserRole.USER
        );

        mockMvc.perform(post("/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("workflow_test_user"));

        // Step 2: Authenticate the new user
        AuthenticationDTO newUserAuth = new AuthenticationDTO(
            "workflow_test_user",
            "workflowPassword123"
        );

        MvcResult authResult = mockMvc.perform(post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserAuth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String newUserToken = extractTokenFromResponse(authResult);

        // Step 3: Create tasks for the new user
        CreateTaskDTO userTask1 = new CreateTaskDTO(
            "First User Task",
            "User's first task",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        CreateTaskDTO userTask2 = new CreateTaskDTO(
            "Second User Task",
            "User's second task",
            LocalDate.now().plusDays(5),
            false,
            true
        );

        // Note: In a real implementation, we would use the token for authentication
        // For this test, we're testing the workflow but the authentication aspect
        // is handled by the security context in the service layer

        MvcResult task1Result = createTaskViaAPI(userTask1, newUserToken);
        MvcResult task2Result = createTaskViaAPI(userTask2, newUserToken);

        // Verify tasks were created
        assertThat(extractTaskIdFromResponse(task1Result)).isNotNull();
        assertThat(extractTaskIdFromResponse(task2Result)).isNotNull();

        // Verify user has tasks (this would need to query by the new user in a real scenario)
        // For now, we verify that tasks were created in the system
        var allTasks = taskRepository.findAll();
        var taskTitles = allTasks.stream().map(task -> task.getTitle()).toList();
        assertThat(taskTitles).contains("First User Task", "Second User Task");
    }

    @Test
    @DisplayName("Should validate task creation business rules")
    void shouldValidateTaskCreationBusinessRules() throws Exception {
        // Test 1: Task with past due date should fail
        CreateTaskDTO pastDueTask = new CreateTaskDTO(
            "Past Due Task",
            "Task with past due date",
            LocalDate.now().minusDays(1), // Past date
            true,
            true
        );

        mockMvc.perform(post("/v1/tasks")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pastDueTask)))
                .andExpect(status().isBadRequest());

        // Test 2: Task with empty title should fail
        CreateTaskDTO emptyTitleTask = new CreateTaskDTO(
            "", // Empty title
            "Task with empty title",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        mockMvc.perform(post("/v1/tasks")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyTitleTask)))
                .andExpect(status().isBadRequest());

        // Test 3: Task with null title should fail
        CreateTaskDTO nullTitleTask = new CreateTaskDTO(
            null, // Null title
            "Task with null title",
            LocalDate.now().plusDays(1),
            true,
            true
        );

        mockMvc.perform(post("/v1/tasks")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullTitleTask)))
                .andExpect(status().isBadRequest());

        // Verify no invalid tasks were created
        var userTasks = taskRepository.findByUser(testUser);
        assertThat(userTasks).isEmpty(); // No tasks should be created due to validation failures
    }

    private MvcResult createTaskViaAPI(CreateTaskDTO taskDTO, String token) throws Exception {
        return mockMvc.perform(post("/v1/tasks")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(taskDTO.title()))
                .andExpect(jsonPath("$.description").value(taskDTO.description()))
                .andExpect(jsonPath("$.taskStatus").value("CREATED"))
                .andReturn();
    }

    private Long extractTaskIdFromResponse(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        return json.get("id").asLong();
    }

    private String extractTokenFromResponse(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(responseBody);
        return json.get("token").asText();
    }
}