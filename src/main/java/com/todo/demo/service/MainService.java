package com.todo.demo.service;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.view.console.TaskMenu;
import com.todo.demo.view.console.UserMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MainService {
    private static final Logger logger = LoggerFactory.getLogger(MainService.class);
    private final UserMenu userMenu;
    private final TaskMenu taskMenu;

    public MainService(UserMenu userMenu, TaskMenu taskMenu) {
        this.userMenu = userMenu;
        this.taskMenu = taskMenu;
    }

    public boolean processCommand(String command, UserDTO userDTO) {
        switch (command) {
            case "1", "user", "usermenu" -> {
                userMenu.userMenu(userDTO);
                return false;
            }
            case "2", "task", "taskmenu" -> {
                taskMenu.showTaskMenu(userDTO);
                return false;
            }
            case "0", "logout", "exit" -> {
                logger.info("User {} logged out", userDTO.getName());
                return true;
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                return false;
            }
        }
    }
}