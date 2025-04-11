package com.todo.demo.interfaces;


import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.model.dto.UserUpdateDTO;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract UserEntity map(UserCreateDTO dto);
    public abstract UserDTO map(UserEntity user);
    public abstract void update(UserUpdateDTO dto, @MappingTarget UserEntity user);
}
