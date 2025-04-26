package com.todo.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskCreateDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String priority;
    @NotBlank
    private Long assignee;
    @NotBlank
    private Long author;
    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime deadline;
}
