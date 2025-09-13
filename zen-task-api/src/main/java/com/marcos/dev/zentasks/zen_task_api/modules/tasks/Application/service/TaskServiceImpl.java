package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.dev.zentasks.zen_task_api.common.domain.security.annotations.RequireAuthentication;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.BusinessRuleException;
import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ResourceNotFoundException;
import com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security.AuthenticatedUserService;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.DashboardTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskFilterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.persistence.EntityManager;

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
  @RequireAuthentication(message = "Você deve estar autenticado para visualizar uma tarefa.")
  @Transactional(readOnly = true)
  public TaskResponseDTO getTaskById(Long id) {
    TaskModel task = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    // Verify if the task belongs to the authenticated user
    if (!task.getUser().getId().equals(currentUser.getId())) {
      throw new ResourceNotFoundException("Tarefa não encontrada");
    }

    return taskMapper.toResponseDTO(task);
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

  @Override
  @Transactional
  @RequireAuthentication(message = "Você deve estar autenticado para deletar uma tarefa.")
  public void deleteTask(Long id) {
    TaskModel taskToDelete = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    // Verify if the task belongs to the authenticated user
    if (!taskToDelete.getUser().getId().equals(currentUser.getId())) {
      throw new ResourceNotFoundException("Tarefa não encontrada");
    }

    taskRepository.delete(taskToDelete);
  }

  @Transactional
  @RequireAuthentication(message = "Você deve estar autenticado para modificar uma tarefa")
  @Override
  public void moveQuadrant(Long id, MoveQuadrantDTO newQuadrantDTO) {
    TaskModel taskToUpdateQuadrant = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não existe!"));

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    // Verify if the task belongs to the authenticated user
    if (!taskToUpdateQuadrant.getUser().getId().equals(currentUser.getId())) {
      throw new ResourceNotFoundException("Tarefa não encontrada");
    }

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
        .dueDateBetween(LocalDate.MIN, LocalDate.now().minusDays(1))
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

  @Override
  @RequireAuthentication(message = "Você deve estar autenticado para visualizar as tarefas")
  @Transactional(readOnly = true)
  public List<TaskResponseDTO> findTasksByFilter(TaskFilterDTO filter) {
    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    TaskRepository.Specifications.SpecificationBuilder specBuilder = TaskRepository.Specifications.builder()
        .forUser(currentUser);

    if (filter.quadrant() != null) {
      specBuilder.inQuadrant(filter.quadrant());
    }

    if (filter.status() != null) {
      specBuilder.withStatus(filter.status());
    }

    if (filter.fromDate() != null && filter.toDate() != null) {
      specBuilder.dueDateBetween(filter.fromDate(), filter.toDate());
    }

    if (filter.isComplete() != null) {
      specBuilder.isCompleted(filter.isComplete());
    }

    Specification<TaskModel> spec = specBuilder.build();
    List<TaskModel> tasks = taskRepository.findAll(spec);

    return taskMapper.toResponseDTOList(tasks);
  }

  @Override
  @RequireAuthentication(message = "Você deve estar autenticado para visualizar as tarefas")
  @Transactional(readOnly = true)
  public Map<LocalDate, List<TaskResponseDTO>> getWeeklyView(LocalDate weekStartDate) {

    UserModel currentUser = (UserModel) authenticatedUserService
        .getCurrentAuthentication()
        .getPrincipal();

    if (weekStartDate == null) {
      throw new BusinessRuleException("A data de início da semana não pode ser nula");
    }

    LocalDate weekEndDate = weekStartDate.plusDays(6);

    Specification<TaskModel> spec = TaskRepository.Specifications.builder()
        .forUser(currentUser)
        .dueDateBetween(weekStartDate, weekEndDate)
        .build();

    List<TaskModel> tasksInWeek = taskRepository.findAll(spec);
    List<TaskResponseDTO> responseDTOs = taskMapper.toResponseDTOList(tasksInWeek);

    return responseDTOs.stream()
        .collect(Collectors.groupingBy(TaskResponseDTO::dueDate));

  }

}
