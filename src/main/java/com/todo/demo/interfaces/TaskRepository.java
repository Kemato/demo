package com.todo.demo.interfaces;

import com.todo.demo.model.entity.TaskEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    @Query("SELECT * FROM tasks WHERE name=:name")
    Optional <TaskEntity> findByName(String name);
    Optional <TaskEntity> findById(Long id);
    boolean existsById(@NotNull Long id);
    boolean existsByName(@NotNull String name);
}
