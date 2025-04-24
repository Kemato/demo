package com.todo.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String status;
    @NotBlank
    private String priority;
    @NotBlank
    private Long assignee;
    @NotBlank
    private Long author;
    @NotNull
    @FutureOrPresent
    private LocalDateTime deadline;
}
