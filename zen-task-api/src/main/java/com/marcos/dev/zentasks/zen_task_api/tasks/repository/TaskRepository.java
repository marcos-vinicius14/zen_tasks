package com.marcos.dev.zentasks.zen_task_api.tasks.repository;

import com.marcos.dev.zentasks.zen_task_api.tasks.enums.Quadrant;
import com.marcos.dev.zentasks.zen_task_api.tasks.enums.TaskStatus;
import com.marcos.dev.zentasks.zen_task_api.tasks.model.TaskModel;
import com.marcos.dev.zentasks.zen_task_api.users.model.UserModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long>, JpaSpecificationExecutor<TaskModel> {

    /**
     * Specifications builder para consultas dinâmicas de tarefas
     */
    class Specifications {
        private Specifications() {}

        public static SpecificationBuilder builder() {
            return new SpecificationBuilder();
        }

public static class SpecificationBuilder {
    private final AtomicReference<UserModel> user = new AtomicReference<>();
    private final AtomicReference<TaskStatus> status = new AtomicReference<>();
            private Quadrant quadrant;
            private Boolean completed;
            private LocalDate dueDateFrom;
            private LocalDate dueDateTo;
            private Boolean urgent;
            private Boolean important;

            private SpecificationBuilder() {}

            public SpecificationBuilder forUser(UserModel user) {
                if (user == null) {
                    throw new IllegalArgumentException("O usuário não pode ser nulo");
                }
                this.user.set(user);
                return this;
            }

            public SpecificationBuilder withStatus(TaskStatus status) {
                this.status.set(status);
                return this;
            }

            public SpecificationBuilder inQuadrant(Quadrant quadrant) {
                this.quadrant = quadrant;
                return this;
            }

            public SpecificationBuilder isCompleted(Boolean completed) {
                this.completed = completed;
                return this;
            }

            public SpecificationBuilder dueDateBetween(LocalDate from, LocalDate to) {
                this.dueDateFrom = from;
                this.dueDateTo = to;
                return this;
            }

            public SpecificationBuilder isUrgent(Boolean urgent) {
                this.urgent = urgent;
                return this;
            }

            public SpecificationBuilder isImportant(Boolean important) {
                this.important = important;
                return this;
            }

            public org.springframework.data.jpa.domain.Specification<TaskModel> build() {
                return (root, query, cb) -> {
                    ArrayList<Predicate> predicates = new ArrayList<>();
    
                    if (user != null) {
                        predicates.add(cb.equal(root.get("user"), user));
                    }

                    if (status != null) {
                        predicates.add(cb.equal(root.get("status"), status));
                    }

                    if (quadrant != null) {
                        predicates.add(cb.equal(root.get("quadrant"), quadrant));
                    }

                    if (completed != null) {
                        predicates.add(cb.equal(root.get("isCompleted"), completed));
                    }

                    if (dueDateFrom != null && dueDateTo != null) {
                        predicates.add(cb.between(root.get("dueDate"), dueDateFrom, dueDateTo));
                    } else if (dueDateFrom != null) {
                        predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), dueDateFrom));
                    } else if (dueDateTo != null) {
                        predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), dueDateTo));
                    }

                    if (urgent != null) {
                        predicates.add(cb.equal(root.get("isUrgent"), urgent));
                    }

                    if (important != null) {
                        predicates.add(cb.equal(root.get("isImportant"), important));
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                };
            }
        }
    }

    List<TaskModel> findByUser(UserModel user);
    Optional<TaskModel> findByIdAndUser(Long id, UserModel user);

    @Query("SELECT t FROM TaskModel t WHERE t.user = :user AND t.quadrant = :quadrant")
    List<TaskModel> findByUserAndQuadrant(@Param("user") UserModel user, @Param("quadrant") Quadrant quadrant);

    @Query("SELECT COUNT(t) FROM TaskModel t WHERE t.user = :user AND t.isCompleted = false")
    long countIncompleteTasksByUser(@Param("user") UserModel user);

    @Query("SELECT t FROM TaskModel t WHERE t.user = :user AND t.dueDate <= :date AND t.isCompleted = false")
    List<TaskModel> findOverdueTasks(@Param("user") UserModel user, @Param("date") LocalDate date);

    default List<TaskModel> findTasksBySpecification(Specifications.SpecificationBuilder specBuilder) {
        return findAll(specBuilder.build());
    }
}