package com.todo.demo.interfaces;


import com.todo.demo.model.UserCreateDTO;
import com.todo.demo.model.UserDTO;
import com.todo.demo.model.UserEntity;
import com.todo.demo.model.UserUpdateDTO;
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
