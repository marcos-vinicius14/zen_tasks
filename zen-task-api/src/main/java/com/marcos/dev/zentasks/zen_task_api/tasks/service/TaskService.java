package com.marcos.dev.zentasks.zen_task_api.tasks.service;

import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.UpdateTaskDTO;

public interface TaskService {
  TaskResponseDTO createNewTask(CreateTaskDTO data);

  void editTask(UpdateTaskDTO data);

}
