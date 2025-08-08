package com.marcos.dev.zentasks.zen_task_api.tasks.model;

import com.marcos.dev.zentasks.zen_task_api.tasks.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.tasks.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_tasks")
public class TaskModel {
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
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "quadrant", nullable = false, length = 20)
    private Quadrant quadrant;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    public TaskModel() {
    }

    public TaskModel(String title, String description, LocalDate dueDate, boolean isUrgent, boolean isImportant, boolean isCompleted, LocalDateTime completedAt, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isUrgent = isUrgent;
        this.isImportant = isImportant;
        this.isCompleted = isCompleted;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UserModel getUser() {
        return user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel taskModel = (TaskModel) o;
        return isUrgent == taskModel.isUrgent && isImportant == taskModel.isImportant && isCompleted == taskModel.isCompleted && Objects.equals(id, taskModel.id) && Objects.equals(title, taskModel.title) && Objects.equals(description, taskModel.description) && Objects.equals(dueDate, taskModel.dueDate) && Objects.equals(createdAt, taskModel.createdAt) && Objects.equals(completedAt, taskModel.completedAt);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
