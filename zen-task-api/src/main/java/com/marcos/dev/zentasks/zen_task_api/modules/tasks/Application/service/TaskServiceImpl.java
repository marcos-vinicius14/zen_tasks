package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.marcos.dev.zentasks.zen_task_api.common.domain.security.annotations.RequireAuthentication;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security.AuthenticatedUserService;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.DashboardTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

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
  public void editTask(Long id, UpdateTaskDTO data) {
    TaskModel taskToUpdate = taskRepository.findById(id)
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

  @Transactional
  @RequireAuthentication(message = "Você deve estar autenticado para modificar uma tarefa")
  @Override
  public void moveQuadrant(Long id, MoveQuadrantDTO newQuadrantDTO) {
    TaskModel taskToUpdateQuadrant = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não existe!"));

    taskToUpdateQuadrant.moveTo(newQuadrantDTO.newQuadrant());

    taskRepository.save(taskToUpdateQuadrant);
  }

  @Override
  @RequireAuthentication(message = "Você deve estar autenticado para visualizar as tarefas")
  @Transactional(readOnly = true)
  public DashboardTaskDTO getDashboardTasks() {

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    Specification<TaskModel> overdueSpec = TaskRepository.Specifications.builder()
        .forUser(currentUser)
        .dueDateBetween(null, LocalDate.now().minusDays(1))
        .isCompleted(false)
        .build();

    List<TaskModel> overdueTasks = taskRepository.findAll(overdueSpec);

    Specification<TaskModel> dueTodaySpec = TaskRepository.Specifications.builder()
        .forUser(currentUser)
        .dueDateBetween(LocalDate.now(), LocalDate.now())
        .isCompleted(false)
        .build();

    List<TaskModel> dueTodayTasks = taskRepository.findAll(dueTodaySpec);

    Specification<TaskModel> doNowSpec = TaskRepository.Specifications.builder()
        .forUser(currentUser)
        .inQuadrant(Quadrant.DO_NOW)
        .isCompleted(false)
        .build();

    List<TaskModel> doNowTasks = taskRepository.findAll(doNowSpec);

    List<TaskResponseDTO> overdueTasksDtos = taskMapper.toResponseDTOList(overdueTasks);
    List<TaskResponseDTO> todayTasksDtos = taskMapper.toResponseDTOList(dueTodayTasks);
    List<TaskResponseDTO> doNowTasksDtos = taskMapper.toResponseDTOList(doNowTasks);

    return new DashboardTaskDTO(overdueTasksDtos, todayTasksDtos, doNowTasksDtos);

  }
}
