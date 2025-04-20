package com.todo.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
        @NotNull
        private Long id;
        private Optional <String> title = Optional.empty();
        private Optional <String> description = Optional.empty();
        private Optional <String> status = Optional.empty();
        private Optional <String> priority = Optional.empty();
        private Optional <Long> assigned = Optional.empty();
        private Optional <LocalDateTime> deadline = Optional.empty();
        private Optional <LocalDateTime> dateFinished = Optional.empty();
}
