package com.todo.demo.view.console;

import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.service.TaskMenuService;
import com.todo.demo.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Console-based menu for task management.
 */
@Component
public class TaskMenu {
    private final TaskMenuService taskMenuService;
    private final Scanner scanner;

    @Autowired
    public TaskMenu(TaskMenuService taskMenuService, Scanner scanner) {
        this.taskMenuService = taskMenuService;
        this.scanner = scanner;
    }

    public void showTaskMenu(UserDTO user) {
        while (true) {
            boolean hasTasks = taskMenuService.hasTasks();
            System.out.printf("""
                    Task Menu:
                    %s
                    Enter choice: """, taskMenuService.getMenuOptions(hasTasks));
            String choice = scanner.nextLine();
            if (choice == null || choice.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            if (taskMenuService.processCommand(choice.toLowerCase(), user)) {
                return; // Выход из меню
            }
        }
    }
}