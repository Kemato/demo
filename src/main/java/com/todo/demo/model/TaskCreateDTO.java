package com.todo.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO {
    String title;
    String description;
    String status;
    String priority;
    String assignee;
    String author;
    LocalDate deadline;
}
