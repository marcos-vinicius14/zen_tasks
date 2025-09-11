package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.DashboardTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

public interface TaskService {
  TaskResponseDTO createNewTask(CreateTaskDTO data);

  void editTask(Long id, UpdateTaskDTO data);

  void moveQuadrant(Long id, MoveQuadrantDTO newQuadrant);

  DashboardTaskDTO getDashboardTasks();

}
