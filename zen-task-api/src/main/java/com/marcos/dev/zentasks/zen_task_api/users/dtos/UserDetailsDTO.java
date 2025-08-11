package com.marcos.dev.zentasks.zen_task_api.users.dtos;

import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.users.enums.UserRole;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO para detalhes do usuário
 */
public record UserDetailsDTO(
        UUID id,
        String username,
        String email,
        UserRole role,
        LocalDateTime createdAt,
        UserStats stats
) {

    /**
     * Classe interna para estatísticas do usuário
     */
    public record UserStats(
            long totalTasks,
            long completedTasks,
            long pendingTasks,
            LocalDate lastTaskCreated,
            LocalDate lastTaskCompleted
    ) {
        public static UserStats from(UserModel user) {
            Set<TaskModel> tasks = user.getTasks();
            long completed = tasks.stream()
                    .filter(TaskModel::isCompleted)
                    .count();

            return new UserStats(
                    tasks.size(),
                    completed,
                    tasks.size() - completed,
                    tasks.stream()
                            .map(TaskModel::getCreatedAt)
                            .max(LocalDateTime::compareTo)
                            .map(LocalDateTime::toLocalDate)
                            .orElse(null),
                    tasks.stream()
                            .filter(TaskModel::isCompleted)
                            .map(TaskModel::getCompletedAt)
                            .max(LocalDateTime::compareTo)
                            .map(LocalDateTime::toLocalDate)
                            .orElse(null)
            );
        }
    }
}