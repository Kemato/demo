package com.todo.demo.interfaces;

import com.todo.demo.model.TaskEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface TaskRepository extends CrudRepository<TaskEntity, Integer> {
    @Query("SELECT * FROM tasks WHERE name=:name")
    Optional <TaskEntity> findByName(String name);
}
