package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;

public interface TaskService {
  TaskResponseDTO createNewTask(CreateTaskDTO data);

  void editTask(UpdateTaskDTO data);

}
