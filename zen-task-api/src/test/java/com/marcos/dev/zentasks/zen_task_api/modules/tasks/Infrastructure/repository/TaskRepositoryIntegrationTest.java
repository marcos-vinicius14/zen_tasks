package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.factories.UserFactory;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Task Repository Integration Tests")
class TaskRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private UserModel testUser;
    private UserModel anotherUser;

    @BeforeEach
    void setUp() {
        testUser = UserFactory.create(
            "Test User",
            "test@example.com", 
            "password123"
        );
        anotherUser = UserFactory.create(
            "Another User",
            "another@example.com",
            "password456"
        );
        
        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(anotherUser);
    }

    @Test
    @DisplayName("Should save and find task by ID")
    void shouldSaveAndFindTaskById() {
        // Given
        TaskModel task = TaskModel.builder()
            .title("Integration Test Task")
            .description("Testing repository integration")
            .dueDate(LocalDate.now().plusDays(1))
            .urgent(true)
            .important(true)
            .user(testUser)
            .build();

        // When
        TaskModel savedTask = taskRepository.save(task);
        entityManager.flush();
        Optional<TaskModel> foundTask = taskRepository.findById(savedTask.getId());

        // Then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Integration Test Task");
        assertThat(foundTask.get().getQuadrant()).isEqualTo(Quadrant.DO_NOW);
        assertThat(foundTask.get().getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Should find tasks by user")
    void shouldFindTasksByUser() {
        // Given
        TaskModel task1 = createTask("Task 1", testUser, Quadrant.DO_NOW);
        TaskModel task2 = createTask("Task 2", testUser, Quadrant.SCHEDULE);
        TaskModel task3 = createTask("Task 3", anotherUser, Quadrant.DO_NOW);

        entityManager.persistAndFlush(task1);
        entityManager.persistAndFlush(task2);
        entityManager.persistAndFlush(task3);

        // When
        List<TaskModel> userTasks = taskRepository.findByUser(testUser);

        // Then
        assertThat(userTasks).hasSize(2);
        assertThat(userTasks).extracting(TaskModel::getTitle).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    @DisplayName("Should find task by ID and user")
    void shouldFindTaskByIdAndUser() {
        // Given
        TaskModel task = createTask("User Specific Task", testUser, Quadrant.DELEGATE);
        TaskModel savedTask = entityManager.persistAndFlush(task);

        // When
        Optional<TaskModel> foundTask = taskRepository.findByIdAndUser(savedTask.getId(), testUser);
        Optional<TaskModel> notFoundTask = taskRepository.findByIdAndUser(savedTask.getId(), anotherUser);

        // Then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("User Specific Task");
        assertThat(notFoundTask).isEmpty();
    }

    @Test
    @DisplayName("Should find tasks by user and quadrant")
    void shouldFindTasksByUserAndQuadrant() {
        // Given
        TaskModel doNowTask = createTask("Urgent Important", testUser, Quadrant.DO_NOW);
        TaskModel scheduleTask = createTask("Important Not Urgent", testUser, Quadrant.SCHEDULE);
        TaskModel delegateTask = createTask("Urgent Not Important", testUser, Quadrant.DELEGATE);

        entityManager.persistAndFlush(doNowTask);
        entityManager.persistAndFlush(scheduleTask);
        entityManager.persistAndFlush(delegateTask);

        // When
        List<TaskModel> doNowTasks = taskRepository.findByUserAndQuadrant(testUser, Quadrant.DO_NOW);
        List<TaskModel> scheduleTasks = taskRepository.findByUserAndQuadrant(testUser, Quadrant.SCHEDULE);

        // Then
        assertThat(doNowTasks).hasSize(1);
        assertThat(doNowTasks.get(0).getTitle()).isEqualTo("Urgent Important");
        
        assertThat(scheduleTasks).hasSize(1);
        assertThat(scheduleTasks.get(0).getTitle()).isEqualTo("Important Not Urgent");
    }

    @Test
    @DisplayName("Should count incomplete tasks by user")
    void shouldCountIncompleteTasksByUser() {
        // Given
        TaskModel incompleteTask1 = createTask("Incomplete 1", testUser, Quadrant.DO_NOW);
        TaskModel incompleteTask2 = createTask("Incomplete 2", testUser, Quadrant.SCHEDULE);
        TaskModel completedTask = createTask("Completed", testUser, Quadrant.DO_NOW);
        completedTask.updateStatus(TaskStatus.DONE);

        entityManager.persistAndFlush(incompleteTask1);
        entityManager.persistAndFlush(incompleteTask2);
        entityManager.persistAndFlush(completedTask);

        // When
        long incompleteCount = taskRepository.countIncompleteTasksByUser(testUser);

        // Then
        assertThat(incompleteCount).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find tasks using specifications - by status")
    void shouldFindTasksUsingSpecifications_ByStatus() {
        // Given
        TaskModel createdTask = createTask("Created Task", testUser, Quadrant.DO_NOW);
        TaskModel inProgressTask = createTask("In Progress Task", testUser, Quadrant.SCHEDULE);
        inProgressTask.updateStatus(TaskStatus.IN_PROGRESS);

        entityManager.persistAndFlush(createdTask);
        entityManager.persistAndFlush(inProgressTask);

        // When
        Specification<TaskModel> spec = TaskRepository.Specifications.builder()
            .forUser(testUser)
            .withStatus(TaskStatus.IN_PROGRESS)
            .build();

        List<TaskModel> tasks = taskRepository.findAll(spec);

        // Then
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("In Progress Task");
        assertThat(tasks.get(0).getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Should find tasks using specifications - by quadrant and priority")
    void shouldFindTasksUsingSpecifications_ByQuadrantAndPriority() {
        // Given
        TaskModel urgentImportant = createTask("Urgent Important", testUser, Quadrant.DO_NOW);
        TaskModel urgentNotImportant = createTask("Urgent Not Important", testUser, Quadrant.DELEGATE);
        TaskModel notUrgentImportant = createTask("Not Urgent Important", testUser, Quadrant.SCHEDULE);

        entityManager.persistAndFlush(urgentImportant);
        entityManager.persistAndFlush(urgentNotImportant);
        entityManager.persistAndFlush(notUrgentImportant);

        // When
        Specification<TaskModel> spec = TaskRepository.Specifications.builder()
            .forUser(testUser)
            .inQuadrant(Quadrant.DO_NOW)
            .isUrgent(true)
            .isImportant(true)
            .build();

        List<TaskModel> tasks = taskRepository.findAll(spec);

        // Then
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Urgent Important");
        assertThat(tasks.get(0).getQuadrant()).isEqualTo(Quadrant.DO_NOW);
    }

    @Test
    @DisplayName("Should find tasks using specifications - by due date range")
    void shouldFindTasksUsingSpecifications_ByDueDateRange() {
        // Given
        LocalDate today = LocalDate.now();
        TaskModel taskToday = createTaskWithDueDate("Task Today", testUser, today);
        TaskModel taskTomorrow = createTaskWithDueDate("Task Tomorrow", testUser, today.plusDays(1));
        TaskModel taskNextWeek = createTaskWithDueDate("Task Next Week", testUser, today.plusDays(7));

        entityManager.persistAndFlush(taskToday);
        entityManager.persistAndFlush(taskTomorrow);
        entityManager.persistAndFlush(taskNextWeek);

        // When
        Specification<TaskModel> spec = TaskRepository.Specifications.builder()
            .forUser(testUser)
            .dueDateBetween(today, today.plusDays(2))
            .build();

        List<TaskModel> tasks = taskRepository.findAll(spec);

        // Then
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(TaskModel::getTitle)
            .containsExactlyInAnyOrder("Task Today", "Task Tomorrow");
    }

    @Test
    @DisplayName("Should find tasks using specifications - by completion status")
    void shouldFindTasksUsingSpecifications_ByCompletionStatus() {
        // Given
        TaskModel completedTask = createTask("Completed Task", testUser, Quadrant.DO_NOW);
        completedTask.updateStatus(TaskStatus.DONE);
        TaskModel incompleteTask = createTask("Incomplete Task", testUser, Quadrant.SCHEDULE);

        entityManager.persistAndFlush(completedTask);
        entityManager.persistAndFlush(incompleteTask);

        // When
        Specification<TaskModel> spec = TaskRepository.Specifications.builder()
            .forUser(testUser)
            .isCompleted(true)
            .build();

        List<TaskModel> tasks = taskRepository.findAll(spec);

        // Then
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Completed Task");
        assertThat(tasks.get(0).isCompleted()).isTrue();
    }

    private TaskModel createTask(String title, UserModel user, Quadrant quadrant) {
        return TaskModel.builder()
            .title(title)
            .description("Test description for " + title)
            .dueDate(LocalDate.now().plusDays(1))
            .urgent(quadrant == Quadrant.DO_NOW || quadrant == Quadrant.DELEGATE)
            .important(quadrant == Quadrant.DO_NOW || quadrant == Quadrant.SCHEDULE)
            .user(user)
            .build();
    }

    private TaskModel createTaskWithDueDate(String title, UserModel user, LocalDate dueDate) {
        return TaskModel.builder()
            .title(title)
            .description("Test description for " + title)
            .dueDate(dueDate)
            .urgent(false)
            .important(false)
            .user(user)
            .build();
    }
}