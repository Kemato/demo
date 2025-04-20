package com.todo.demo.interfaces;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.entity.TaskEntity;
import com.todo.demo.model.dto.TaskUpdateDTO;
import org.mapstruct.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {



    TaskEntity toEntity(TaskCreateDTO dto);

    TaskDTO toDTO(TaskEntity Task);

    default void toEntity(@NotNull TaskUpdateDTO taskUpdateDTO, @NotNull @MappingTarget TaskEntity taskEntity) {
        if(taskUpdateDTO == null){
            throw new IllegalArgumentException("taskUpdateDTO cannot be null");
        }
        taskUpdateDTO.getTitle().ifPresent(taskEntity::setTitle);
        taskUpdateDTO.getDescription().ifPresent(taskEntity::setDescription);

        taskUpdateDTO.getAssigned().ifPresent(taskEntity::setAssigned);
        taskUpdateDTO.getDeadline().ifPresent(taskEntity::setDeadline);

        taskUpdateDTO.getPriority().ifPresent(taskEntity::setPriority);
        taskUpdateDTO.getStatus().ifPresent(taskEntity::setStatus);

        taskUpdateDTO.getDateFinished().ifPresent(taskEntity::setDateFinished);
        taskEntity.setDateUpdated(LocalDateTime.now());
    }
}
