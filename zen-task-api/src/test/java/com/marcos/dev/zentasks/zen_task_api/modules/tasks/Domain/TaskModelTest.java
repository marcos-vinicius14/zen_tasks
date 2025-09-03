package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.BusinessRuleException;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

class TaskModelTest {
  private UserModel mockUser;

  @BeforeEach
  void setup() {

    mockUser = mock(UserModel.class);
  }

  @Test
  void shouldCreateTaskWithValidData() {
    LocalDate futureDate = LocalDate.now().plusDays(7);

    TaskModel task = TaskModel.builder()
        .title("Implementar feature")
        .description("Descrição detalhada")
        .dueDate(futureDate)
        .urgent(true)
        .important(true)
        .user(mockUser)
        .build();

    assertThat(task.getTitle()).isEqualTo("Implementar feature");
    assertThat(task.getDescription()).isEqualTo("Descrição detalhada");
    assertThat(task.getDueDate()).isEqualTo(futureDate);
    assertThat(task.isUrgent()).isTrue();
    assertThat(task.isImportant()).isTrue();
    assertThat(task.getQuadrant()).isEqualTo(Quadrant.DO_NOW);
    assertThat(task.getStatus()).isEqualTo(TaskStatus.CREATED);
    assertThat(task.isCompleted()).isFalse();
    assertThat(task.getUser()).isEqualTo(mockUser);
    assertThat(task.getCreatedAt()).isNotNull();
  }

  @Test
  void shouldDetermineCorrectQuadrants() {
    LocalDate futureDate = LocalDate.now().plusDays(1);

    // DO_NOW: urgent=true, important=true
    TaskModel doNowTask = TaskModel.builder()
        .title("Urgent Important Task")
        .description("Description")
        .dueDate(futureDate)
        .urgent(true)
        .important(true)
        .user(mockUser)
        .build();
    assertThat(doNowTask.getQuadrant()).isEqualTo(Quadrant.DO_NOW);

    // SCHEDULE: urgent=false, important=true
    TaskModel scheduleTask = TaskModel.builder()
        .title("Important Task")
        .description("Description")
        .dueDate(futureDate)
        .urgent(false)
        .important(true)
        .user(mockUser)
        .build();
    assertThat(scheduleTask.getQuadrant()).isEqualTo(Quadrant.SCHEDULE);

    // DELEGATE: urgent=true, important=false
    TaskModel delegateTask = TaskModel.builder()
        .title("Urgent Task")
        .description("Description")
        .dueDate(futureDate)
        .urgent(true)
        .important(false)
        .user(mockUser)
        .build();
    assertThat(delegateTask.getQuadrant()).isEqualTo(Quadrant.DELEGATE);

    // ELIMINATE: urgent=false, important=false
    TaskModel eliminateTask = TaskModel.builder()
        .title("Neither Task")
        .description("Description")
        .dueDate(futureDate)
        .urgent(false)
        .important(false)
        .user(mockUser)
        .build();
    assertThat(eliminateTask.getQuadrant()).isEqualTo(Quadrant.ELIMINATE);
  }

  @Test
  void shouldThrowExceptionWhenTitleIsNull() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title(null)
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task title cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionWhenTitleIsBlank() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title("   ")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task title cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionWhenDescriptionIsNull() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title("Title")
        .description(null)
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task description cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionWhenDueDateIsNull() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title("Title")
        .description("Description")
        .dueDate(null)
        .user(mockUser)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task due date cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenUserIsNull() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title("Title")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(null)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("User cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenDueDateIsInPast() {
    assertThatThrownBy(() -> TaskModel.builder()
        .title("Title")
        .description("Description")
        .dueDate(LocalDate.now().minusDays(1))
        .user(mockUser)
        .build())
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Due date cannot be in the past");
  }

  @Test
  void shouldMoveTaskToAnotherQuadrant() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .urgent(false)
        .important(false)
        .user(mockUser)
        .build();

    assertThat(task.getQuadrant()).isEqualTo(Quadrant.ELIMINATE);

    // When
    task.moveTo(Quadrant.DO_NOW);

    // Then
    assertThat(task.getQuadrant()).isEqualTo(Quadrant.DO_NOW);
    assertThat(task.isUrgent()).isTrue();
    assertThat(task.isImportant()).isTrue();
  }

  @Test
  void shouldThrowExceptionWhenMovingToSameQuadrant() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .urgent(true)
        .important(true)
        .user(mockUser)
        .build();

    // When & Then
    assertThatThrownBy(() -> task.moveTo(Quadrant.DO_NOW))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task is already in this quadrant");
  }

  @Test
  void shouldThrowExceptionWhenMovingCompletedTask() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .urgent(true)
        .important(true)
        .user(mockUser)
        .build();

    task.updateStatus(TaskStatus.DONE);

    // When & Then
    assertThatThrownBy(() -> task.moveTo(Quadrant.SCHEDULE))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Cannot modify a completed task");
  }

  @Test
  void shouldUpdateTaskDetails() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Old Title")
        .description("Old Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build();

    LocalDate newDueDate = LocalDate.now().plusDays(5);

    // When
    boolean wasUpdated = task.updateDetails("New Title", "New Description", newDueDate);

    // Then
    assertThat(wasUpdated).isTrue();
    assertThat(task.getTitle()).isEqualTo("New Title");
    assertThat(task.getDescription()).isEqualTo("New Description");
    assertThat(task.getDueDate()).isEqualTo(newDueDate);
  }

  @Test
  void shouldReturnFalseWhenNoChangesInUpdate() {
    // Given
    LocalDate dueDate = LocalDate.now().plusDays(1);
    TaskModel task = TaskModel.builder()
        .title("Title")
        .description("Description")
        .dueDate(dueDate)
        .user(mockUser)
        .build();

    // When
    boolean wasUpdated = task.updateDetails("Title", "Description", dueDate);

    // Then
    assertThat(wasUpdated).isFalse();
  }

  @Test
  void shouldUpdateTaskStatus() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build();

    assertThat(task.getStatus()).isEqualTo(TaskStatus.CREATED);
    assertThat(task.isCompleted()).isFalse();
    assertThat(task.getCompletedAt()).isNull();

    // When
    task.updateStatus(TaskStatus.DONE);

    // Then
    assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    assertThat(task.isCompleted()).isTrue();
    assertThat(task.getCompletedAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenUpdatingClosedTask() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build();

    task.updateStatus(TaskStatus.CLOSED);

    assertThatThrownBy(() -> task.updateStatus(TaskStatus.IN_PROGRESS))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Cannot modify a closed or canceled task");
  }

  @Test
  void shouldSetPriorityAndUpdateQuadrant() {
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .urgent(false)
        .important(false)
        .user(mockUser)
        .build();

    assertThat(task.getQuadrant()).isEqualTo(Quadrant.ELIMINATE);

    task.setPriority(true, true);

    assertThat(task.isUrgent()).isTrue();
    assertThat(task.isImportant()).isTrue();
    assertThat(task.getQuadrant()).isEqualTo(Quadrant.DO_NOW);
  }

  @Test
  void shouldNotChangePriorityWhenSameValues() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .urgent(true)
        .important(false)
        .user(mockUser)
        .build();

    Quadrant originalQuadrant = task.getQuadrant();

    // When
    task.setPriority(true, false); // Same values

    // Then
    assertThat(task.getQuadrant()).isEqualTo(originalQuadrant);
  }

  @Test
  void shouldThrowExceptionWhenUpdatingDetailsWithInvalidData() {
    // Given
    TaskModel task = TaskModel.builder()
        .title("Task")
        .description("Description")
        .dueDate(LocalDate.now().plusDays(1))
        .user(mockUser)
        .build();

    // When & Then - título nulo
    assertThatThrownBy(() -> task.updateDetails(null, "Description", LocalDate.now().plusDays(2)))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task title cannot be null or empty");

    // When & Then - descrição nula
    assertThatThrownBy(() -> task.updateDetails("Title", null, LocalDate.now().plusDays(2)))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Task description cannot be null or empty");

    // When & Then - data no passado
    assertThatThrownBy(() -> task.updateDetails("Title", "Description", LocalDate.now().minusDays(1)))
        .isInstanceOf(BusinessRuleException.class)
        .hasMessage("Due date cannot be in the past");
  }
}
