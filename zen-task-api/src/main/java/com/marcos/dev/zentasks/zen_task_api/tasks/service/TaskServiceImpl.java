package com.marcos.dev.zentasks.zen_task_api.tasks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.tasks.repository.TaskRepository;

import jakarta.transaction.Transactional;

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
  @Transactional
  public TaskResponseDTO createNewTask(CreateTaskDTO data) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {

      logger.warn("Usuário não autenticado.");
      throw new ForbiddenAccessException("[TaskService] Usuário não autenticado.");

    }

    logger.info("[TASKSERVICE] O Usuário {} esta criando a tarefa {}", authentication.getName(), data.title());

    TaskModel savedTask = taskMapper.toEntity(data);
    taskRepository.save(savedTask);

    TaskResponseDTO result = taskMapper.toResponseDTO(savedTask);

    logger.debug("[TASKSERVICE] Task {} criada com sucesso!", result.id());

    return result;
  }

  @Override
  public void editTask(UpdateTaskDTO data) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'editTask'");
  }
}
