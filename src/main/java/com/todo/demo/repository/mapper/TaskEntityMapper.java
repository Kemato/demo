package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import org.mapstruct.*;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TaskEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateFinished", ignore = true)
    @Mapping(target = "status", ignore = true)
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
