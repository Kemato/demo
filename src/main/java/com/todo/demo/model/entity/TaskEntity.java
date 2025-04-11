package com.todo.demo.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
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
