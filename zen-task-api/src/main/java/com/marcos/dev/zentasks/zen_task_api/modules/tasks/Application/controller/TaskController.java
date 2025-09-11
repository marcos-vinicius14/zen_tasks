package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.MoveQuadrantDTO;
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

  @GetMapping
  public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
    return null;
  }
}
