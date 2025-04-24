package com.todo.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    Long id;
    String title;
    String description;
    String status;
    String priority;
    String assignee;
    String author;
    LocalDateTime dateCreated;
    LocalDateTime dateUpdated;
    LocalDateTime deadline;
    LocalDateTime dateFinished;
}
