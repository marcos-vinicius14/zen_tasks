package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.domain.security.annotations.RequireAuthentication;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security.AuthenticatedUserService;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final AuthenticatedUserService authenticatedUserService;

  private final EntityManager entityManager;

  public TaskServiceImpl(
      TaskRepository taskRepository,
      TaskMapper taskMapper,
      AuthenticatedUserService authenticatedUserService,
      EntityManager entityManager) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
    this.authenticatedUserService = authenticatedUserService;
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  @RequireAuthentication(message = "Usuário deve estar autenticado para criar uma nova tarefa!")
  public TaskResponseDTO createNewTask(CreateTaskDTO data) {
    UUID userID = authenticatedUserService.getCurrentUserId();
    UserModel userReference = entityManager.getReference(UserModel.class, userID);

    TaskModel taskToSave = taskMapper.toEntity(data, userReference);

    taskRepository.save(taskToSave);

    TaskResponseDTO result = taskMapper.toResponseDTO(taskToSave);

    return result;

  }

  @Override
  @Transactional
  @RequireAuthentication(message = "Você deve estar autenticado para atualizar uma nova tarefa.")
  public void editTask(UpdateTaskDTO data) {
    TaskModel taskToUpdate = taskRepository.findById(data.taskId())
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontada"));

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    taskToUpdate.updateDetails(
        data.title(),
        data.description(),
        data.dueDate(),
        currentUser);

    taskRepository.save(taskToUpdate);
  }

  @RequireAuthentication(message = "Você deve estar autenticado para modificar uma tarefa")
  public void moveQuadrant(Quadrant targeQuadrant) {

  }
}
