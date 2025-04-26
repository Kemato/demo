package com.todo.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отображения пользователей")
public class UserDTO {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "Пётр")
    private String name;
}
