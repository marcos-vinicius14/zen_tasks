package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos;

import java.util.List;

public record DashboardTaskDTO(
    List<TaskResponseDTO> overdueTasks,
    List<TaskResponseDTO> todayTasks,
    List<TaskResponseDTO> doNowTasks) {
}
