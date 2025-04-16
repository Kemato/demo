package com.todo.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    LocalDate dateCreated;
    LocalDate dateUpdated;
    LocalDate deadline;
    LocalDate dateFinished;
}
