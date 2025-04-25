package com.todo.demo.service;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import com.todo.demo.repository.TaskRepository;
import com.todo.demo.repository.mapper.TaskEntityMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskEntityMapper taskEntityMapper;

    public TaskService(TaskRepository repository, TaskEntityMapper taskEntityMapper) {
        this.taskRepository = repository;
        this.taskEntityMapper = taskEntityMapper;
    }

    public TaskDTO createTask(@NotNull TaskCreateDTO taskCreateDTO) {
        try {
            Optional<TaskDTO> exitingTask = taskRepository.findByTitle(taskCreateDTO.getTitle());
            if (exitingTask.isPresent()) {
                throw new IllegalArgumentException("Task already exists");
            }
            TaskEntity taskEntity = taskEntityMapper.toEntity(taskCreateDTO);
            taskEntity.setDateUpdated(LocalDateTime.now());
            taskEntity.setDateCreated(LocalDateTime.now());
            taskEntity.setStatus("CREATED");
            return taskRepository.save(taskEntity);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error creating task" + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull Long id) {
        try {
            Optional<TaskDTO> exitingTask = taskRepository.findById(id);
            if (exitingTask.isPresent()) {
                return exitingTask.get();
            } else {
                throw new IllegalArgumentException("Task does not exist");
            }
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error reading task" + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull String title) {
        try {
            return taskRepository.findByTitle(title).orElseThrow(() -> new IllegalArgumentException("Task does not exist"));
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error reading task" + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTasks() {
        try {
            ArrayList<TaskDTO> taskDTOArrayList = new ArrayList<>(taskRepository.findAll());
            if (taskDTOArrayList.isEmpty()) {
                System.out.println("No tasks found");
//                throw new IllegalArgumentException("No tasks found");
            }
            return taskDTOArrayList;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error reading all tasks", ex);
        }
    }

    public boolean deleteTask(@NotNull Long id) {
        try {
            return taskRepository.delete(id);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Could not delete task" + ex.getMessage(), ex);
        }
    }

    public Optional<TaskDTO> updateTask(@NotNull TaskUpdateDTO taskUpdateDTO) {
        try {
            Optional<TaskEntity> taskEntityOptional = taskRepository.findEntityById(taskUpdateDTO.getId());
            if (taskEntityOptional.isEmpty()) {
                throw new IllegalArgumentException("Task does not exist");
            }
            TaskEntity taskEntity = taskEntityOptional.get();
            taskEntityMapper.toEntity(taskUpdateDTO, taskEntity);
            return taskRepository.update(taskEntity);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Could not update task" + ex.getMessage(), ex);
        }
    }


    public ArrayList<TaskDTO> readAllTaskByAssignee(@NotNull Long userId) {
        try {
            return new ArrayList<>(taskRepository.findAllByAssignee(userId));
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error reading all tasks" + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTaskByAuthor(@NotNull Long userId) {
        try {
            return new ArrayList<>(taskRepository.findAllByAuthor(userId));
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error reading all tasks" + ex.getMessage(), ex);
        }
    }
}
