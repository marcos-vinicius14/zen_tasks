package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

  private static Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  @PersistenceContext
  private EntityManager entityManager;

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

    UUID authenticatedUserId = getUserIdFromAutentication(authentication);

    logger.debug("User ID obtido da autenticação: {}", authenticatedUserId);

    UserModel userReference = entityManager.getReference(UserModel.class, authenticatedUserId);

    TaskModel taskToSave = taskMapper.toEntity(data, userReference);

    logger.debug("Task após setar user: {}", taskToSave.getUser());

    logger.debug("User ID na task: {}", taskToSave.getUser() != null ? taskToSave.getUser().getId() : "NULL");

    taskRepository.save(taskToSave);

    TaskResponseDTO result = taskMapper.toResponseDTO(taskToSave);

    logger.debug("[TASKSERVICE] Task {} criada com sucesso!", result.id());

    return result;
  }

  @Override
  public void editTask(UpdateTaskDTO data) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'editTask'");
  }

  private UUID getUserIdFromAutentication(Authentication authentication) {
    logger.debug("=== EXTRAINDO USER ID ===");
    logger.debug("Authentication principal type: {}", authentication.getPrincipal().getClass());

    UserModel userDetails = (UserModel) authentication.getPrincipal();

    return userDetails.getId();
  }
}
