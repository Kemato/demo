package com.todo.demo.services;

import com.todo.demo.interfaces.TaskRepository;
import com.todo.demo.model.Task;
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
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setStatus("CREATED");
            task.setAssignee(assignee);
            task.setAuthor(author);
            task.setDateCreated(LocalDate.now());
            task.setDateUpdated(LocalDate.now());
            task.setDeadline(deadline);

            taskRepository.save(task);
        }
        else {
            throw new TaskExeption("Task already exists");
        }
    }

}
