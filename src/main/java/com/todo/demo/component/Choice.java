package com.todo.demo.component;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.TaskPriorityEnum;
import com.todo.demo.service.UserService;
import com.todo.demo.view.console_service.SafePromtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Scanner;

import static com.todo.demo.component.StringUtils.capitalizeWords;

@Component
public class Choice {

    private final UserService userService;
    private final Scanner scanner;
    private final SafePromtService safePromtService;

    @Autowired
    public Choice(UserService userService, Scanner scanner, SafePromtService safePromtService) {
        this.userService = userService;
        this.scanner = scanner;
        this.safePromtService = safePromtService;
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

    public Long choiceAssignee() {
        ArrayList<UserDTO> userList = userService.readAllUsers();
        for (int i = 0; i < userList.size(); i++) System.out.println(i + 1 + ". " + userList.get(i).getName());

        return safePromtService.safePrompt(() -> {
            System.out.println("Please enter the assignee name/number: ");
            String input = scanner.nextLine();

            if (input == null || input.isBlank()) throw new IllegalArgumentException("Assignee name cannot be empty.");

            if (input.matches("\\d+")) {
                int index = Integer.parseInt(input);
                if (index < 1 || index > userList.size()) throw new IllegalArgumentException("Assignee number must be between 1 and " + (userList.size() - 1));
                return userList.get(index).getId();
            }
            return userList.stream()
                    .filter(user -> user.getName().equals(input))
                    .findFirst()
                    .map(UserDTO::getId)
                    .orElseThrow(() -> new IllegalArgumentException("Incorrect name"));
        });
    }
}
