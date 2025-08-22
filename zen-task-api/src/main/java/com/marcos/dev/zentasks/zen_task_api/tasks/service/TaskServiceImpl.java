package com.marcos.dev.zentasks.zen_task_api.tasks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.tasks.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

  private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  @Override
  public void createNewTask(CreateTaskDTO data) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {

      logger.warn("Usuário não autenticado.");
      throw new ForbiddenAccessException("[TaskService] Usuário não autenticado.");

    }

    throw new UnsupportedOperationException("Unimplemented method 'createNewTask'");
  }

  @Override
  public void editTask(UpdateTaskDTO data) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'editTask'");
  }
}
