package com.marcos.dev.zentasks.zen_task_api.tasks.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.BusinessRuleException;
import com.marcos.dev.zentasks.zen_task_api.tasks.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.tasks.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a Task in the system.
 * This class manages task details, status, and priority based on the Eisenhower
 * Matrix.
 */
@Entity
@Table(name = "tb_tasks")
public class TaskModel {
  private static final Logger logger = LoggerFactory.getLogger(TaskModel.class);

  private static final String ERROR_TITLE_REQUIRED = "Task title cannot be null or empty";
  private static final String ERROR_DESCRIPTION_REQUIRED = "Task description cannot be null or empty";
  private static final String ERROR_DUE_DATE_REQUIRED = "Task due date cannot be null";
  private static final String ERROR_USER_REQUIRED = "User cannot be null";
  private static final String ERROR_PAST_DUE_DATE = "Due date cannot be in the past";
  private static final String ERROR_COMPLETED_TASK = "Cannot modify a completed task";
  private static final String ERROR_CLOSED_TASK = "Cannot modify a closed or canceled task";
  private static final String ERROR_QUADRANT_REQUIRED = "Quadrant cannot be null";
  private static final String ERROR_SAME_QUADRANT = "Task is already in this quadrant";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id")
  private Long id;

  @Column(name = "title", nullable = false, length = 150)
  private String title;

  @Column(name = "description", length = 2500, nullable = false)
  private String description;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "is_urgent")
  private boolean isUrgent;

  @Column(name = "is_important")
  private boolean isImportant;

  @Column(name = "is_completed")
  private boolean isCompleted;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private TaskStatus status = TaskStatus.CREATED;

  @Enumerated(EnumType.STRING)
  @Column(name = "quadrant", nullable = false, length = 20)
  private Quadrant quadrant;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserModel user;

  private TaskModel() {
  }

  public static TaskBuilder builder() {
    return new TaskBuilder();
  }

  /**
   * Moves the task to a different quadrant in the Eisenhower Matrix
   * 
   * @param targetQuadrant the quadrant to move the task to
   * @throws BusinessRuleException if the task cannot be moved
   */
  public void moveTo(Quadrant targetQuadrant) {
    validateTaskModification();

    if (targetQuadrant == null) {
      throw new BusinessRuleException(ERROR_QUADRANT_REQUIRED);
    }

    if (this.quadrant == targetQuadrant) {
      throw new BusinessRuleException(ERROR_SAME_QUADRANT);
    }

    Quadrant oldQuadrant = this.quadrant;
    assignQuadrant(targetQuadrant);

    logger.info("Task moved from {} to {} - ID: {}, Title: {}",
        oldQuadrant, targetQuadrant, id, title);
  }

  /**
   * Updates the basic details of the task.
   * 
   * @return true if any changes were made, false otherwise
   */
  public boolean updateDetails(String newTitle, String newDescription, LocalDate newDueDate) {
    validateInput(newTitle, newDescription, newDueDate);
    validateTaskModification();

    if (!hasChanges(newTitle, newDescription, newDueDate)) {
      logger.debug("No changes detected for task update");
      return false;
    }

    applyChanges(newTitle, newDescription, newDueDate);
    logger.info("Updated task details - ID: {}, Title: {}", id, newTitle);
    return true;
  }

  /**
   * Updates the status of the task.
   */
  public void updateStatus(TaskStatus newStatus) {
    if (status == TaskStatus.CLOSED || status == TaskStatus.CANCELED) {
      throw new BusinessRuleException(ERROR_CLOSED_TASK);
    }

    TaskStatus oldStatus = this.status;
    this.status = newStatus;

    if (newStatus == TaskStatus.DONE) {
      this.isCompleted = true;
      this.completedAt = LocalDateTime.now();
    }

    logger.info("Task status changed from {} to {} - ID: {}", oldStatus, newStatus, id);
  }

  private void validateInput(String title, String description, LocalDate dueDate) {
    if (title == null || title.isBlank()) {
      throw new BusinessRuleException(ERROR_TITLE_REQUIRED);
    }
    if (description == null || description.isBlank()) {
      throw new BusinessRuleException(ERROR_DESCRIPTION_REQUIRED);
    }
    if (dueDate == null) {
      throw new BusinessRuleException(ERROR_DUE_DATE_REQUIRED);
    }
    validateDueDate(dueDate);
  }

  private void validateDueDate(LocalDate dueDate) {
    if (dueDate.isBefore(LocalDate.now())) {
      throw new BusinessRuleException(ERROR_PAST_DUE_DATE);
    }
  }

  private void validateTaskModification() {
    if (isCompleted) {
      throw new BusinessRuleException(ERROR_COMPLETED_TASK);
    }
  }

  private boolean hasChanges(String newTitle, String newDescription, LocalDate newDueDate) {
    return !newTitle.equals(title) ||
        !newDescription.equals(description) ||
        !newDueDate.equals(dueDate);
  }

  private void applyChanges(String newTitle, String newDescription, LocalDate newDueDate) {
    this.title = newTitle;
    this.description = newDescription;
    this.dueDate = newDueDate;
  }

  private void assignQuadrant(Quadrant newQuadrant) {
    this.quadrant = newQuadrant;
    this.isUrgent = newQuadrant == Quadrant.DO_NOW || newQuadrant == Quadrant.DELEGATE;
    this.isImportant = newQuadrant == Quadrant.DO_NOW || newQuadrant == Quadrant.SCHEDULE;
  }

  public void setPriority(boolean isUrgent, boolean isImportant) {
    validateTaskModification();

    if (this.isUrgent == isUrgent && this.isImportant == isImportant) {
      return;
    }

    this.isUrgent = isUrgent;
    this.isImportant = isImportant;

    Quadrant newQuadrant = determineQuadrant(isUrgent, isImportant);
    if (this.quadrant != newQuadrant) {
      Quadrant oldQuadrant = this.quadrant;
      assignQuadrant(newQuadrant);
      logger.info("Task quadrant changed from {} to {} due to priority update - ID: {}, Title: {}",
          oldQuadrant, newQuadrant, id, title);
    }
  }

  private Quadrant determineQuadrant(boolean isUrgent, boolean isImportant) {
    if (isUrgent && isImportant)
      return Quadrant.DO_NOW;
    if (!isUrgent && isImportant)
      return Quadrant.SCHEDULE;
    if (isUrgent && !isImportant)
      return Quadrant.DELEGATE;
    return Quadrant.ELIMINATE;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    logger.debug("Task creation timestamp set: {}\n", createdAt);
  }

  public static class TaskBuilder {
    private final TaskModel task;

    private TaskBuilder() {
      task = new TaskModel();
      task.createdAt = LocalDateTime.now();
      task.status = TaskStatus.CREATED;
    }

    public TaskBuilder title(String title) {
      task.title = title;
      return this;
    }

    public TaskBuilder description(String description) {
      task.description = description;
      return this;
    }

    public TaskBuilder dueDate(LocalDate dueDate) {
      task.dueDate = dueDate;
      return this;
    }

    public TaskBuilder urgent(boolean isUrgent) {
      task.isUrgent = isUrgent;
      return this;
    }

    public TaskBuilder important(boolean isImportant) {
      task.isImportant = isImportant;
      return this;
    }

    public TaskBuilder user(UserModel user) {
      task.user = user;
      return this;
    }

    public TaskBuilder quadrant(Quadrant quadrant) {
      if (quadrant != null) {
        task.quadrant = quadrant;
        task.isUrgent = quadrant == Quadrant.DO_NOW || quadrant == Quadrant.DELEGATE;
        task.isImportant = quadrant == Quadrant.DO_NOW || quadrant == Quadrant.SCHEDULE;
      }
      return this;
    }

    public TaskModel build() {
      validateTask();
      if (task.quadrant == null) {
        task.quadrant = determineQuadrant(task.isUrgent, task.isImportant);
      }
      logger.info("Created new task: {} with due date: {}\n", task.title, task.dueDate);
      return task;
    }

    private void validateTask() {
      if (task.title == null || task.title.isBlank()) {
        throw new BusinessRuleException(ERROR_TITLE_REQUIRED);
      }
      if (task.description == null || task.description.isBlank()) {
        throw new BusinessRuleException(ERROR_DESCRIPTION_REQUIRED);
      }
      if (task.dueDate == null) {
        throw new BusinessRuleException(ERROR_DUE_DATE_REQUIRED);
      }
      if (task.user == null) {
        throw new BusinessRuleException(ERROR_USER_REQUIRED);
      }
      if (task.dueDate.isBefore(LocalDate.now())) {
        throw new BusinessRuleException(ERROR_PAST_DUE_DATE);
      }
    }

    private Quadrant determineQuadrant(boolean isUrgent, boolean isImportant) {
      if (isUrgent && isImportant)
        return Quadrant.DO_NOW;
      if (!isUrgent && isImportant)
        return Quadrant.SCHEDULE;
      if (isUrgent && !isImportant)
        return Quadrant.DELEGATE;
      return Quadrant.ELIMINATE;
    }
  }

  // Getters
  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public boolean isUrgent() {
    return isUrgent;
  }

  public boolean isImportant() {
    return isImportant;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public Quadrant getQuadrant() {
    return quadrant;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public UserModel getUser() {
    return user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    TaskModel taskModel = (TaskModel) o;
    return Objects.equals(id, taskModel.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "TaskModel{"
        + "id=" + id +
        ", title='" + title + "'" +
        ", status=" + status +
        ", quadrant=" + quadrant +
        ", dueDate=" + dueDate +
        '}';
  }
}
