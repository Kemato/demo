package com.todo.demo.interfaces;


import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.model.dto.UserUpdateDTO;
import org.mapstruct.*;

import javax.validation.constraints.NotNull;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    UserDTO toDTO(UserEntity userEntity);

    UserEntity toEntity(UserCreateDTO userCreateDTO);

    default void toEntity(@NotNull UserUpdateDTO userUpdateDTO, @NotNull @MappingTarget UserEntity existingEntity) {
        if (userUpdateDTO == null) {
            throw new IllegalArgumentException("userUpdateDTO cannot be null");
        }
        userUpdateDTO.getName().ifPresent(existingEntity::setName);
        userUpdateDTO.getPassword().ifPresent(existingEntity::setPassword);
    }
}
