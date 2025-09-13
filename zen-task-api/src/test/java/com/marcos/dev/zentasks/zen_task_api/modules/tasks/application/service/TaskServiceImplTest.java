package com.marcos.dev.zentasks.zen_task_api.modules.tasks.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marcos.dev.zentasks.zen_task_api.common.exceptions.ForbiddenAccessException;
import com.marcos.dev.zentasks.zen_task_api.common.infraestructure.security.AuthenticatedUserService;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers.TaskMapper;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service.TaskServiceImpl;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Infrastructure.repository.TaskRepository;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
  @Mock
  private AuthenticatedUserService authenticatedUserService;

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private EntityManager entityManager;

  @Mock
  private TaskMapper taskMapper;

  @InjectMocks
  private TaskServiceImpl taskService;

  @Test
  void shouldCreateTaskSucessfully() {
    UUID userId = UUID.randomUUID();
    String username = "TesteUserName";
    CreateTaskDTO createTaskDTO = new CreateTaskDTO(
        "Implementar feature X",
        "Descrição detalhada da feature",
        LocalDate.now().plusDays(7),
        true,
        true);

    UserModel userReference = mock(UserModel.class);
    TaskModel createdTask = mock(TaskModel.class);

    TaskResponseDTO expectedResponse = new TaskResponseDTO(
        1L,
        "Implementar feature X",
        "Descrição detalhada da feature",
        LocalDate.now().plusDays(7),
        TaskStatus.CREATED,
        Quadrant.DO_NOW,
        false
    );

    when(authenticatedUserService.getCurrentUserId())
        .thenReturn(userId);

    when(entityManager.getReference(UserModel.class, userId))
        .thenReturn(userReference);

    when(taskMapper.toEntity(createTaskDTO, userReference))
        .thenReturn(createdTask);

    when(taskRepository.save(createdTask))
        .thenReturn(createdTask);

    when(taskMapper.toResponseDTO(createdTask))
        .thenReturn(expectedResponse);

    TaskResponseDTO result = taskService.createNewTask(createTaskDTO);

    assertThat(result).isEqualTo(expectedResponse);
    verify(authenticatedUserService).getCurrentUserId();
    verify(taskMapper).toEntity(createTaskDTO, userReference);
    verify(taskRepository).save(createdTask);
    verify(taskMapper).toResponseDTO(createdTask);
  }

  @Test
  void shouldThrowExceptionWhenUserNotAuthenticated() {
    CreateTaskDTO createDTO = new CreateTaskDTO(
        "Implementar testes de Unidade",
        "Implementar testes para o application service",
        LocalDate.now().plusDays(7),
        true,
        true);

    when(authenticatedUserService.getCurrentUserId())
        .thenThrow(new ForbiddenAccessException("Usuário não autenticado"));

    assertThatThrownBy(() -> taskService.createNewTask(createDTO))
        .isInstanceOf(ForbiddenAccessException.class)
        .hasMessage("Usuário não autenticado");

    verify(authenticatedUserService).getCurrentUserId();
    verifyNoInteractions(taskRepository, taskMapper, entityManager);
  }

}
