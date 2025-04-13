package com.todo.demo.services;

import com.todo.demo.interfaces.TaskMapper;
import com.todo.demo.interfaces.TaskRepository;
import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository repository, TaskMapper taskMapper) {
        this.taskRepository = repository;
        this.taskMapper = taskMapper;
    }

    public TaskDTO createTask(@NotNull TaskCreateDTO taskCreateDTO) {
        try{
            Optional <TaskEntity> exitingTask = taskRepository.findByTitle(taskCreateDTO.getTitle());
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
            Optional<TaskEntity> exitingTask = taskRepository.findByTitle(title);
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
            taskRepository.findAll().forEach(taskEntity -> taskDTOArrayList.add(taskMapper.toDTO(taskEntity)));
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
            return false;
        }
    }

    public boolean existTask(@NotNull String title) {
        try {
            return taskRepository.existsByName(title);
        }
        catch(DataAccessException ex){
            return false;
        }
    }

    public ArrayList<TaskDTO> readAllTaskByUser(@NotNull Long userId) {
        try{
            ArrayList <TaskDTO>  taskDTOArrayList = new ArrayList<>();
            taskRepository.findAllByAssigned(userId).forEach(taskEntity -> taskDTOArrayList.add(taskMapper.toDTO(taskEntity)));
            return taskDTOArrayList;
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error reading all tasks"+ ex.getMessage(), ex);
        }
    }

    public ArrayList<TaskDTO> readAllTaskByAuthor(@NotNull Long userId) {
        try{
            ArrayList <TaskDTO>  taskDTOArrayList = new ArrayList<>();
            taskRepository.findAllByAuthor(userId).forEach(taskEntity -> taskDTOArrayList.add(taskMapper.toDTO(taskEntity)));
            return taskDTOArrayList;
        }
        catch(DataAccessException ex){
            throw new RuntimeException("Error reading all tasks" + ex.getMessage(), ex);
        }
    }
}
