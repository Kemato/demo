package com.todo.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class UserUpdateDTO {
    @NotNull
    private Long id;
    private Optional <String> name = Optional.empty();
    private Optional <String> password = Optional.empty();
}
