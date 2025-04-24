package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import org.mapstruct.*;

import javax.validation.constraints.NotNull;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    UserEntity toEntity(UserCreateDTO userCreateDTO);

    default void toEntity(@NotNull UserUpdateDTO userUpdateDTO, @NotNull @MappingTarget UserEntity existingEntity) {
        userUpdateDTO.getName().ifPresent(existingEntity::setName);
        userUpdateDTO.getPassword().ifPresent(existingEntity::setPassword);
    }
}
