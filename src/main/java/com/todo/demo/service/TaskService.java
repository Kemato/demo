package com.todo.demo.service;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import com.todo.demo.repository.TaskRepository;
import com.todo.demo.repository.mapper.TaskEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskEntityMapper taskEntityMapper;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository repository, TaskEntityMapper taskEntityMapper) {
        this.taskRepository = repository;
        this.taskEntityMapper = taskEntityMapper;
    }

    public TaskDTO createTask(@NotNull TaskCreateDTO taskCreateDTO) {
        try {
            Optional<TaskDTO> exitingTask = taskRepository.findByTitle(taskCreateDTO.getTitle());
            if (exitingTask.isPresent()) {
                logger.warn("Task already exists with title: {}", taskCreateDTO.getTitle());
                throw new IllegalArgumentException("Task already exists");
            }
            TaskEntity taskEntity = taskEntityMapper.toEntity(taskCreateDTO);
            taskEntity.setDateUpdated(LocalDateTime.now());
            taskEntity.setDateCreated(LocalDateTime.now());
            taskEntity.setStatus("CREATED");
            TaskDTO savedTask = taskRepository.save(taskEntity);
            logger.info("Task created successfully with id: {}", savedTask.getId());
            return savedTask;
        } catch (DataAccessException ex) {
            logger.error("Error creating task: {}", taskCreateDTO, ex);
            throw new RuntimeException("Error creating task: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error creating task: {}", taskCreateDTO, ex);
            throw new RuntimeException("Unexpected error creating task: " + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull Long id) {
        try {
            Optional<TaskDTO> exitingTask = taskRepository.findById(id);
            if (exitingTask.isPresent()) {
                logger.debug("Task found with id: {}", id);
                return exitingTask.get();
            } else {
                logger.warn("Task not found with id: {}", id);
                throw new IllegalArgumentException("Task does not exist");
            }
        } catch (DataAccessException ex) {
            logger.error("Error reading task with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Error reading task: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading task with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading task: " + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull String title) {
        try {
            Optional<TaskDTO> task = taskRepository.findByTitle(title);
            if (task.isPresent()) {
                logger.debug("Task found with title: {}", title);
                return task.get();
            } else {
                logger.warn("Task not found with title: {}", title);
                throw new IllegalArgumentException("Task does not exist");
            }
        } catch (DataAccessException ex) {
            logger.error("Error reading task with title {}: {}", title, ex.getMessage(), ex);
            throw new RuntimeException("Error reading task: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading task with title {}: {}", title, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading task: " + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTasks() {
        try {
            ArrayList<TaskDTO> taskDTOArrayList = new ArrayList<>(taskRepository.findAll());
            if (taskDTOArrayList.isEmpty()) {
                logger.info("No tasks found");
            } else {
                logger.debug("Found {} tasks", taskDTOArrayList.size());
            }
            return taskDTOArrayList;
        } catch (DataAccessException ex) {
            logger.error("Error reading all tasks: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error reading all tasks: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading all tasks: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading all tasks: " + ex.getMessage(), ex);
        }
    }

    public boolean deleteTask(@NotNull Long id) {
        try {
            boolean deleted = taskRepository.delete(id);
            if (deleted) {
                logger.info("Task deleted successfully with id: {}", id);
            } else {
                logger.warn("Task not found for deletion with id: {}", id);
            }
            return deleted;
        } catch (DataAccessException ex) {
            logger.error("Error deleting task with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Could not delete task: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error deleting task with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error deleting task: " + ex.getMessage(), ex);
        }
    }

    public Optional<TaskDTO> updateTask(@NotNull TaskUpdateDTO taskUpdateDTO) {
        try {
            Optional<TaskEntity> taskEntityOptional = taskRepository.findEntityById(taskUpdateDTO.getId());
            if (taskEntityOptional.isEmpty()) {
                logger.warn("Task not found for update with id: {}", taskUpdateDTO.getId());
                throw new IllegalArgumentException("Task does not exist");
            }
            TaskEntity taskEntity = taskEntityOptional.get();
            taskEntityMapper.toEntity(taskUpdateDTO, taskEntity);
            taskEntity.setDateUpdated(LocalDateTime.now());
            Optional<TaskDTO> updatedTask = taskRepository.update(taskEntity);
            if (updatedTask.isPresent()) {
                logger.info("Task updated successfully with id: {}", taskUpdateDTO.getId());
            }
            return updatedTask;
        } catch (DataAccessException ex) {
            logger.error("Error updating task with id {}: {}", taskUpdateDTO.getId(), ex.getMessage(), ex);
            throw new RuntimeException("Could not update task: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error updating task with id {}: {}", taskUpdateDTO.getId(), ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error updating task: " + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTaskByAssignee(@NotNull Long userId) {
        try {
            ArrayList<TaskDTO> tasks = new ArrayList<>(taskRepository.findAllByAssignee(userId));
            logger.debug("Found {} tasks for assignee with id: {}", tasks.size(), userId);
            return tasks;
        } catch (DataAccessException ex) {
            logger.error("Error reading tasks for assignee with id {}: {}", userId, ex.getMessage(), ex);
            throw new RuntimeException("Error reading all tasks: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading tasks for assignee with id {}: {}", userId, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading all tasks: " + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTaskByAuthor(@NotNull Long userId) {
        try {
            ArrayList<TaskDTO> tasks = new ArrayList<>(taskRepository.findAllByAuthor(userId));
            logger.debug("Found {} tasks for author with id: {}", tasks.size(), userId);
            return tasks;
        } catch (DataAccessException ex) {
            logger.error("Error reading tasks for author with id {}: {}", userId, ex.getMessage(), ex);
            throw new RuntimeException("Error reading all tasks: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading tasks for author with id {}: {}", userId, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading all tasks: " + ex.getMessage(), ex);
        }
    }
}