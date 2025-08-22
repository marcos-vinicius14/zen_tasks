package com.marcos.dev.zentasks.zen_task_api.tasks.mappers;

import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
  public TaskModel toEntity(CreateTaskDTO dto) {
    logger.debug("Converting CreateTaskDTO to TaskModel: {}", dto.title());

    return TaskModel.builder()
        .title(dto.title())
        .description(dto.description())
        .dueDate(dto.dueDate())
        .important(dto.isImportant())
        .urgent(dto.isUrgent())
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
        entity.isUrgent(),
        entity.isImportant(),
        entity.isCompleted(),
        entity.getCreatedAt(),
        entity.getCompletedAt());
  }

  /**
   * Updates an existing TaskModel entity with data from an UpdateTaskDTO.
   * Only non-null fields in the DTO will be used to update the entity.
   *
   * @param dto    The DTO containing the fields to be updated.
   * @param entity The original TaskModel entity to be modified.
   * @return true if any changes were made, false otherwise
   */
  public boolean updateEntityFromDTO(UpdateTaskDTO dto, TaskModel entity) {
    logger.debug("Updating TaskModel from UpdateTaskDTO: {}", entity.getTitle());

    boolean hasBasicChanges = updateBasicDetails(dto, entity);
    boolean hasStatusChanges = updateCompletionStatus(dto, entity);

    return hasBasicChanges || hasStatusChanges;
  }

  private boolean updateBasicDetails(UpdateTaskDTO dto, TaskModel entity) {
    String newTitle = dto.title() != null ? dto.title() : entity.getTitle();
    String newDescription = dto.description() != null ? dto.description() : entity.getDescription();
    var newDueDate = dto.dueDate() != null ? dto.dueDate() : entity.getDueDate();

    if (!newTitle.equals(entity.getTitle()) ||
        !newDescription.equals(entity.getDescription()) ||
        !newDueDate.equals(entity.getDueDate())) {

      return entity.updateDetails(newTitle, newDescription, newDueDate);
    }

    if (shouldUpdatePriority(dto, entity)) {
      boolean isUrgent = dto.isUrgent() != null ? dto.isUrgent() : entity.isUrgent();
      boolean isImportant = dto.isImportant() != null ? dto.isImportant() : entity.isImportant();
      entity.setPriority(isUrgent, isImportant);
      return true;
    }

    return false;
  }

  private boolean shouldUpdatePriority(UpdateTaskDTO dto, TaskModel entity) {
    return (dto.isUrgent() != null && dto.isUrgent() != entity.isUrgent()) ||
        (dto.isImportant() != null && dto.isImportant() != entity.isImportant());
  }

  private boolean updateCompletionStatus(UpdateTaskDTO dto, TaskModel entity) {
    if (dto.isCompleted() == null) {
      return false;
    }

    if (dto.isCompleted() != entity.isCompleted()) {
      entity.updateStatus(dto.isCompleted() ? TaskStatus.DONE : TaskStatus.IN_PROGRESS);
      return true;
    }

    return false;
  }
}
