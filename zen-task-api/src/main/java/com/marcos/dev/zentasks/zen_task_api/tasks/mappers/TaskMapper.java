package com.marcos.dev.zentasks.zen_task_api.tasks.mappers;

import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.CreateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.TaskResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.UpdateTaskDTO;
import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Componente responsável por mapear (converter) objetos entre a camada de
 * DTO (Data Transfer Object) e a camada de Modelo (Entidade).
 * <p>
 * Esta classe centraliza a lógica de conversão, garantindo que as regras de negócio
 * para a criação, atualização e visualização de tarefas sejam aplicadas de forma consistente.
 */
@Component
public class TaskMapper {

    /**
     * Converte um DTO de criação (`CreateTaskDTO`) em uma nova entidade `TaskModel`.
     * <p>
     * Este método é usado ao criar uma nova tarefa. Ele define os valores iniciais
     * padrão, como o status de conclusão e a data de criação.
     *
     * @param dto O DTO contendo os dados da nova tarefa a ser criada.
     * @return Uma nova instância de {@link TaskModel} pronta para ser persistida.
     */
    public TaskModel toEntity(CreateTaskDTO dto) {
        TaskModel taskModel = new TaskModel();

        taskModel.setTitle(dto.title());
        taskModel.setDescription(dto.description());
        taskModel.setDueDate(dto.dueDate());
        taskModel.setImportant(dto.isImportant());
        taskModel.setUrgent(dto.isUrgent());

        // Define os valores padrão para uma nova tarefa
        taskModel.setCompleted(false);
        taskModel.setCompletedAt(null);
        taskModel.setCreatedAt(LocalDateTime.now());

        return  taskModel;
    }

    /**
     * Converte uma entidade `TaskModel` em um DTO de resposta (`TaskResponseDTO`).
     * <p>
     * Este método prepara o objeto que será enviado de volta ao cliente,
     * garantindo que apenas os dados relevantes e seguros sejam expostos.
     *
     * @param entity A entidade {@link TaskModel} vinda do banco de dados.
     * @return Um DTO de resposta contendo os dados da tarefa.
     */
    public TaskResponseDTO toResponseDTO(TaskModel entity) {
        return new TaskResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                entity.isUrgent(),
                entity.isImportant(),
                entity.isCompleted(),
                entity.getCreatedAt(),
                entity.getCompletedAt()
        );
    }

    /**
     * Atualiza uma entidade `TaskModel` existente com os dados de um DTO de atualização (`UpdateTaskDTO`).
     * <p>
     * Este método lida com atualizações parciais: apenas os campos não nulos no DTO
     * serão usados para atualizar a entidade. A lógica é orquestrada para garantir
     * a consistência dos dados.
     *
     * @param dto O DTO contendo os campos a serem atualizados.
     * @param entity A entidade {@link TaskModel} original que será modificada.
     */
    public void updateEntityFromDTO(UpdateTaskDTO dto, TaskModel entity) {
        // Usa Optional para atualizar campos simples apenas se eles estiverem presentes no DTO
        Optional.ofNullable(dto.title())
                .ifPresent(entity::setTitle);

        Optional.ofNullable(dto.description())
                .ifPresent(entity::setDescription);

        Optional.ofNullable(dto.dueDate())
                .ifPresent(entity::setDueDate);

        Optional.ofNullable(dto.isUrgent())
                .ifPresent(entity::setUrgent);

        Optional.ofNullable(dto.isImportant())
                .ifPresent(entity::setImportant);

        // Delega a lógica mais complexa de atualização de status para um método auxiliar
        updateCompletionStatus(dto, entity);
    }

    /**
     * Método auxiliar privado que orquestra a atualização do status de conclusão.
     *
     * @param dto O DTO de atualização.
     * @param entity A entidade a ser modificada.
     */
    private void updateCompletionStatus(UpdateTaskDTO dto, TaskModel entity) {
        Boolean newCompletionStatus = dto.isCompleted();
        if (newCompletionStatus == null) {
            return;
        }

        // A ordem das chamadas é crucial para a lógica funcionar corretamente.
        updateCompletionTimestamp(entity, newCompletionStatus);
        updateTaskCompletionState(entity, newCompletionStatus);
    }

    /**
     * Método auxiliar que define o estado booleano de conclusão da tarefa.
     *
     * @param entity A entidade a ser modificada.
     * @param newCompletionStatus O novo estado de conclusão.
     */
    private void updateTaskCompletionState(TaskModel entity, boolean newCompletionStatus) {
        entity.setCompleted(newCompletionStatus);
    }

    /**
     * Método auxiliar que gerencia a data de conclusão (`completedAt`) usando o padrão "early return".
     *
     * @param entity A entidade a ser modificada.
     * @param newCompletionStatus O novo estado de conclusão.
     */
    private void updateCompletionTimestamp(TaskModel entity, boolean newCompletionStatus) {
        if (newCompletionStatus && entity.isCompleted()) {
            return;
        }

        LocalDateTime newTimestamp = newCompletionStatus
                ? LocalDateTime.now()
                : null;

        entity.setCompletedAt(newTimestamp);
    }
}
