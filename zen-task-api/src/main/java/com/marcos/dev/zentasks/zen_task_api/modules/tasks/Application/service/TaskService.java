package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import java.util.List;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.DashboardTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;

public interface TaskService {
  TaskResponseDTO createNewTask(CreateTaskDTO data);

  void editTask(Long id, UpdateTaskDTO data);

  void moveQuadrant(Long id, MoveQuadrantDTO newQuadrant);

  DashboardTaskDTO getDashboardTasks();

  List<TaskResponseDTO> findTasksByFilter();

}
