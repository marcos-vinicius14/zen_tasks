package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.DashboardTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskFilterDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
  private static Logger logger = LoggerFactory.getLogger(TaskController.class);

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<TaskResponseDTO> createTask(
      @Valid @RequestBody CreateTaskDTO data) {
    logger.info("[TASKCONTROLLER] Recebida a requisição para criar uma nova tarefa");

    TaskResponseDTO createdTask = taskService.createNewTask(data);

    logger.debug("[TASKCONTROLLER] Tarefa {} criada com sucesso", createdTask.title());

    return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> editTask(
      @PathVariable Long id,
      @Valid @RequestBody UpdateTaskDTO data) {

    logger.info("[TASKCONTROLLER] Atualizando tarefa ID: {}", id);

    taskService.editTask(id, data);

    return ResponseEntity
        .noContent()
        .build();
  }

  @PatchMapping("/move/{id}")
  public ResponseEntity<Void> moveQuadrant(
      @PathVariable Long id,
      @Valid @RequestBody MoveQuadrantDTO targetQuadrant) {
    logger.info("[TASKCONTROLLER] Recebida a requisição para mover o quadrante de uma tarefa");

    taskService.moveQuadrant(id, targetQuadrant);
    logger.info("[TASKCONTROLLER] Quadrante da tarefa {} movido com sucesso", id);

    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping("/dashboard")
  public ResponseEntity<DashboardTaskDTO> getDashboardTask() {
    logger.info("[TASKCONTROLLER] Recebida a requisição para obter as tarefas do dashboard");

    DashboardTaskDTO result = taskService.getDashboardTasks();

    logger.info("[TASKCONTROLLER] Tarefas do dashboard obtidas com sucesso");

    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<List<TaskResponseDTO>> findTasksByFilter(TaskFilterDTO filter) {
    logger.info("[TASKCONTROLLER] Recebida a requisição para obter as tarefas por filtro");

    List<TaskResponseDTO> result = taskService.findTasksByFilter(filter);

    logger.info("[TASKCONTROLLER] Tarefas obtidas com sucesso");

    return ResponseEntity.ok(result);

  }

  @GetMapping("/weekly/{weekStartDate}")
  public ResponseEntity<Map<LocalDate, List<TaskResponseDTO>>> getWeeklyView(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate) {

    logger.info("[TASKCONTROLLER] Recebida a requisição para obter a visão semanal das tarefas");

    Map<LocalDate, List<TaskResponseDTO>> result = taskService.getWeeklyView(weekStartDate);

    return ResponseEntity.ok(result);

  }

}
