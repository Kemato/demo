package com.todo.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class TaskEntity {
    @Id
    Long id;
    String title;
    String description;
    String status;
    String priority;
    Long assigned;
    Long author;
    LocalDateTime dateCreated;
    LocalDateTime dateUpdated;
    LocalDateTime deadline;
    LocalDateTime dateFinished;
}
