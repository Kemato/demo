package com.todo.demo.view.console_view;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.view.console_service.TaskMenuService;
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
                    Task Menu:%n
                    %s%n
                    Enter choice:%n
                    """, taskMenuService.getMenuOptions(hasTasks));
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