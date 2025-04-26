package com.todo.demo.service;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.TaskMenuEnum;
import com.todo.demo.model.exception.NotFoundException;
import com.todo.demo.view.console.TaskUpdateMenu;
import com.todo.demo.component.Choice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Service for processing task menu commands.
 */
@Service
public class TaskMenuService {
    private static final Logger logger = LoggerFactory.getLogger(TaskMenuService.class);
    private final TaskService taskService;
    private final TaskUpdateMenu taskUpdateMenu;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Scanner scanner;
    private final Choice choice;

@Autowired
    public TaskMenuService(TaskService taskService,TaskUpdateMenu taskUpdateMenu, Scanner scanner, Choice choice) {
        this.taskService = taskService;
        this.taskUpdateMenu = taskUpdateMenu;
        this.scanner = scanner;
        this.choice = choice;
    }


    public boolean hasTasks() {
        try {
            return !taskService.readAllTasks().isEmpty();
        } catch (Exception e) {
            logger.error("Error checking tasks: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getMenuOptions(boolean hasTasks) {
        if (!hasTasks) {
            return "1. Create";
        }
        StringBuilder options = new StringBuilder();
        for (TaskMenuEnum menu : TaskMenuEnum.values()) {
            options.append(menu.ordinal() + 1).append(". ").append(menu.name().replace("_", " ")).append("\n");
        }
        return options.toString();
    }

    public boolean processCommand(String command, UserDTO user) {
        TaskMenuEnum menu = TaskMenuEnum.fromString(command);
        if (menu == null) {
            System.out.println("Invalid choice. Please try again.");
            return false;
        }

        try {
            switch (menu) {
                case CREATE -> {
                    TaskCreateDTO taskCreateDTO = promptTaskCreateDTO(user);
                    taskCreateDTO.setAuthor(user.getId());
                    taskService.createTask(taskCreateDTO);
                    System.out.println("New task created!");
                }
                case READ -> {
                    ArrayList<TaskDTO> tasks = promptReadTasks(user);
                    if (tasks.isEmpty()) {
                        System.out.println("There are no tasks.");
                    } else {
                        tasks.forEach(this::printTask);
                    }
                    System.out.println("Press any key to continue...");
                    scanner.nextLine();
                }
                case UPDATE -> {
                    ArrayList<TaskDTO> tasksByAuthor = taskService.readAllTaskByAuthor(user.getId());
                    if (tasksByAuthor.isEmpty()) {
                        System.out.println("You have no tasks that you can update.");
                    } else {
                        Long taskId = promptTaskSelection(tasksByAuthor, "update");
                        taskUpdateMenu.taskUpdateMenu(taskId);
                    }
                }
                case DELETE -> {
                    ArrayList<TaskDTO> tasksByAuthor = taskService.readAllTaskByAuthor(user.getId());
                    if (tasksByAuthor.isEmpty()) {
                        System.out.println("You have no tasks that you can delete.");
                    } else {
                        Long taskId = promptTaskSelection(tasksByAuthor, "delete");
                        if (confirmDeletion(taskId)) {
                            if (taskService.deleteTask(taskId)) {
                                System.out.println("Task deleted successfully.");
                            } else {
                                System.out.println("Failed to delete task.");
                            }
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                    }
                }
                case BACK -> {
                    logger.info("User {} returned from task menu", user.getName());
                    return true;
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Command processing failed: {}", e.getMessage(), e);
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            System.out.println("An unexpected error occurred.");
        }
        return false;
    }

    private TaskCreateDTO promptTaskCreateDTO(UserDTO user) {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        System.out.println("Enter title: ");
        taskCreateDTO.setTitle(scanner.nextLine());
        System.out.println("Enter description: ");
        taskCreateDTO.setDescription(scanner.nextLine());
        try {
            taskCreateDTO.setAssignee(choice.choiceAssigned());
            taskCreateDTO.setPriority(choice.choicePriority().name());
        }
        catch (NotFoundException e) {
            System.out.println("User not found.");
        }
        while (true) {
            System.out.println("Enter deadline in format 'dd/MM/yyyy HH:mm': ");
            try {
                LocalDateTime deadline = LocalDateTime.parse(scanner.nextLine(), formatter);
                if (deadline.isAfter(LocalDateTime.now())) {
                    taskCreateDTO.setDeadline(deadline);
                    break;
                } else {
                    System.out.println("Deadline must be in the future.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Use dd/MM/yyyy HH:mm.");
            }
        }
        return taskCreateDTO;
    }

    private ArrayList<TaskDTO> promptReadTasks(UserDTO user) {
        System.out.println("1. All tasks");
        System.out.println("2. My tasks");
        String input = scanner.nextLine();
        if (input.equals("1")) {
            return taskService.readAllTasks();
        } else if (input.equals("2")) {
            return taskService.readAllTaskByAssignee(user.getId());
        } else {
            System.out.println("Invalid input. Showing no tasks.");
            return new ArrayList<>();
        }
    }

    private Long promptTaskSelection(ArrayList<TaskDTO> tasks, String action) {
        System.out.printf("Enter the ID of the task to %s:%n", action);
        tasks.forEach(task -> System.out.println(task.getId() + " " + task.getTitle() + " " + task.getDescription()));
        while (true) {
            try {
                String choice = scanner.nextLine();
                Long taskId = Long.parseLong(choice);
                if (tasks.stream().anyMatch(task -> task.getId().equals(taskId))) {
                    return taskId;
                }
                System.out.println("Please select an existing task.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private boolean confirmDeletion(Long taskId) {
        System.out.println("Task to delete: " + taskService.readTask(taskId));
        System.out.println("Are you sure you want to delete this task? (Yes/No)");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }

    private void printTask(TaskDTO task) {
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Author: " + task.getAuthor());
        System.out.println("Assigned: " + task.getAssignee());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Date Created: " + task.getDateCreated());
        System.out.println("Date Updated: " + task.getDateUpdated());
        System.out.println("Deadline: " + task.getDeadline());
        System.out.println("Date Finished: " + task.getDateFinished());
    }
}