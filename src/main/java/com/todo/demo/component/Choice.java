package com.todo.demo.component;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.TaskPriorityEnum;
import com.todo.demo.service.TaskService;
import com.todo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Scanner;

import static com.todo.demo.component.StringUtils.capitalizeWords;
@Component
public class Choice {

    private final UserService userService;
    private final Scanner scanner;

    @Autowired
    public Choice(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public TaskPriorityEnum choicePriority() {
        while (true) {
            System.out.println("Select priority:");
            for (TaskPriorityEnum priority : TaskPriorityEnum.values()) {
                System.out.println(capitalizeWords(priority.name().toLowerCase()));
            }
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            try {
                return TaskPriorityEnum.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Please try again.");
            }
        }
    }

    public String choiceAssigned() {
        ArrayList<UserDTO> userList = userService.readAllUsers();
        while (true) {
            System.out.println("Select user to assign task to:");
            System.out.println("(Input username)");
            userList.forEach(user -> System.out.println(user.getId() + ": " + user.getName()));
            String input = scanner.nextLine();
            if (input == null || input.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            try {
                if (userList.stream().anyMatch(user -> user.getName().equals(input))) {
                    return input;
                }
                System.out.println("Invalid username. Please select an existing user.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid username.");
            }
        }
    }
}
