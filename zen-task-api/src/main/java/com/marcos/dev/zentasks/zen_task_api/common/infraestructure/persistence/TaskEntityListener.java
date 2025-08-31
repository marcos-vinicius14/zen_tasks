package com.marcos.dev.zentasks.zen_task_api.common.infraestructure.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Domain.model.TaskModel;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;

public class TaskEntityListener {

  private static Logger log = LoggerFactory.getLogger(TaskEntityListener.class);

  @PrePersist
  public void prePersist(TaskModel taskModel) {
    log.debug("Preparando para salvar a task: {} ", taskModel.getTitle());
  }

  @PostPersist
  public void postPersist(TaskModel taskModel) {
    log.debug("Task {} persistida com sucessso", taskModel.getTitle());
  }
}
