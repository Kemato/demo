package com.todo.demo.services;

import com.todo.demo.interfaces.TaskRepository;
import com.todo.demo.model.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.demo.exeption.TaskExeption;

import java.time.LocalDate;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.taskRepository = repository;
    }

    public void createUser(
            String title,
            String description,
            String author,
            String assignee,
            String priority,
            LocalDate deadline

    ) {
        if(taskRepository.findByName(title).isEmpty()){
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTitle(title);
            taskEntity.setDescription(description);
            taskEntity.setPriority(priority);
            taskEntity.setStatus("CREATED");
            taskEntity.setAssignee(assignee);
            taskEntity.setAuthor(author);
            taskEntity.setDateCreated(LocalDate.now());
            taskEntity.setDateUpdated(LocalDate.now());
            taskEntity.setDeadline(deadline);

            taskRepository.save(taskEntity);
        }
        else {
            throw new TaskExeption("Task already exists");
        }
    }

}
