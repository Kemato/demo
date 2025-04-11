package com.todo.demo.interfaces;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.entity.TaskEntity;
import com.todo.demo.model.dto.TaskUpdateDTO;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    public abstract TaskEntity map(TaskCreateDTO dto);
    public abstract TaskDTO map(TaskEntity Task);
    public abstract void update(TaskUpdateDTO dto, @MappingTarget TaskEntity Task);
}
