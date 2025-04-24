package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import org.mapstruct.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TaskEntityMapper {

    TaskEntity toEntity(TaskCreateDTO taskCreateDTO);

    default void toEntity(@NotNull TaskUpdateDTO taskUpdateDTO, @NotNull @MappingTarget TaskEntity taskEntity) {
        taskUpdateDTO.getTitle().ifPresent(taskEntity::setTitle);
        taskUpdateDTO.getStatus().ifPresent(taskEntity::setStatus);
        taskUpdateDTO.getPriority().ifPresent(taskEntity::setPriority);
        taskUpdateDTO.getAssignee().ifPresent(taskEntity::setAssignee);
        taskUpdateDTO.getDescription().ifPresent(taskEntity::setDescription);
        taskUpdateDTO.getDeadline().ifPresent(taskEntity::setDeadline);
        taskUpdateDTO.getDateFinished().ifPresent(taskEntity::setDateFinished);
        taskEntity.setDateUpdated(LocalDateTime.now());
    }
}
