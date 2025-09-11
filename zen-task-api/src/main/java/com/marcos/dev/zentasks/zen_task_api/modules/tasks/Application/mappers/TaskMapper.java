package com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.mappers;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.modules.users.Domain.model.UserModel;

/**
 * Component responsible for mapping objects between DTO and Model layers.
 */
@Component
public class TaskMapper {
  private static final Logger logger = LoggerFactory.getLogger(TaskMapper.class);

  /**
   * Converts a CreateTaskDTO into a new TaskModel entity using the Builder
   * pattern.
   *
   * @param dto The DTO containing the data for the new task.
   * @return A new TaskModel instance ready to be persisted.
   */
  public TaskModel toEntity(CreateTaskDTO dto, UserModel userReference) {
    logger.debug("Converting CreateTaskDTO to TaskModel: {}", dto.title());

    return TaskModel.builder()
        .title(dto.title())
        .description(dto.description())
        .dueDate(dto.dueDate())
        .important(dto.isImportant())
        .urgent(dto.isUrgent())
        .user(userReference)
        .build();
  }

  /**
   * Converts a TaskModel entity to a response DTO.
   *
   * @param entity The TaskModel entity from the database.
   * @return A response DTO containing the task data.
   */
  public TaskResponseDTO toResponseDTO(TaskModel entity) {
    logger.debug("Converting TaskModel to TaskResponseDTO: {}", entity.getTitle());

    return new TaskResponseDTO(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getDueDate(),
        entity.getStatus(),
        entity.getQuadrant(),
        entity.isCompleted());
  }

  public List<TaskResponseDTO> toResponseDTOList(List<TaskModel> entities) {
    logger.debug("Converting List<TaskModel> to List<TaskResponseDTO>");

    if (entities == null) {
      return List.of();
    }

    return entities.stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

}
