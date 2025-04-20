package com.todo.demo.interfaces;

import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.entity.TaskEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
//    @Query("SELECT * FROM tasks WHERE name=:name")

    @Query("""
    SELECT 
        t.id,
        t.title,
        t.status,
        t.author,
        u1.name AS author_name,
        t.priority,
        t.deadline,
        t.assigned,
        u2.name AS assigned_name,
        t.description,
        t.date_created,
        t.date_updated,
        t.date_finished
    FROM tasks t
    JOIN users u1 ON t.author = u1.id
    LEFT JOIN users u2 ON t.assigned = u2.id
    WHERE t.id = :id
""")
    Optional<TaskDTO> readById(@NotNull Long id);


    Optional <TaskEntity> findByTitle(String title);
    Optional <TaskEntity> findById(@NotNull Long id);
    boolean existsById(@NotNull Long id);
    boolean existsByTitle(@NotNull String name);
    ArrayList<TaskEntity> findAllByAssigned(@NotNull Long id);
    ArrayList<TaskEntity> findAllByAuthor(@NotNull Long id);
}
