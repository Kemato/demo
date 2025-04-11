package com.todo.demo.services;

import com.todo.demo.interfaces.TaskMapper;
import com.todo.demo.interfaces.TaskRepository;
import com.todo.demo.interfaces.UserMapper;
import com.todo.demo.interfaces.UserRepository;
import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository repository, TaskMapper taskMapper, UserRepository userRepository) {
        this.taskRepository = repository;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
    }

    public TaskDTO createTask(@NotNull TaskCreateDTO taskCreateDTO) {
        try{
            Optional <TaskEntity> exitingTask = taskRepository.findByName(taskCreateDTO.getTitle());
            if (exitingTask.isPresent()) {
                throw new IllegalArgumentException("Task already exists");
            }
            TaskEntity taskEntity = taskMapper.toEntity(taskCreateDTO);
            return taskMapper.toDTO(taskRepository.save(taskEntity));
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error creating task" + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull Long id) {
        try{
            Optional<TaskEntity> exitingTask = taskRepository.findById(id);
            if (exitingTask.isPresent()) {
                return taskMapper.toDTO(exitingTask.get());
            }
            else{
                throw new IllegalArgumentException("Task does not exist");
            }
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error reading task" + ex.getMessage(), ex);
        }
    }

    public TaskDTO readTask(@NotNull String title) {
        try{
            Optional<TaskEntity> exitingTask = taskRepository.findByName(title);
            if (exitingTask.isPresent()) {
                return taskMapper.toDTO(exitingTask.get());
            }
            else{
                throw new IllegalArgumentException("Task does not exist");
            }
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error reading task" + ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTasks() {
        try{
            ArrayList <TaskDTO>  taskDTOArrayList = new ArrayList<>();
            taskRepository.findAll().forEach(taskDTO -> taskDTOArrayList.add(taskMapper.toDTO(taskDTO)));
            if(taskDTOArrayList.isEmpty()){
                throw new IllegalArgumentException("No tasks found");
            }
            return taskDTOArrayList;
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error reading all tasks", ex);
        }
    }

    public boolean deleteTask(@NotNull Long id) {
        try{
            if(taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
                return true;
            }
            else {
                throw new IllegalArgumentException("Task does not exist");
            }
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Could not delete task" + ex.getMessage(), ex);
        }
    }

    public TaskDTO updateTask(@NotNull TaskUpdateDTO taskUpdateDTO) {
        try{
            Optional <TaskEntity> taskEntityOptional = taskRepository.findById(taskUpdateDTO.getId());
            if(taskEntityOptional.isEmpty()) {
                throw new IllegalArgumentException("Task does not exist");
            }
            TaskEntity taskEntity = taskEntityOptional.get();
            taskMapper.toEntity(taskUpdateDTO, taskEntity);
            TaskEntity updatedTaskEntity = taskRepository.save(taskEntity);
            return taskMapper.toDTO(updatedTaskEntity);
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Could not update task" + ex.getMessage(), ex);
        }
    }

    public boolean existTask(@NotNull Long id) {
        try {
            return taskRepository.existsById(id);
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Could not check if task exists" + ex.getMessage(), ex);
        }
    }

    public boolean existTask(@NotNull String title) {
        try {
            return taskRepository.existsByName(title);

        }
        catch(DataAccessException ex){
            throw new RuntimeException("Could not find task with title " + ex.getMessage(), ex);
        }
    }
}
