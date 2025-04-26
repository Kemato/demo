package com.todo.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
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
