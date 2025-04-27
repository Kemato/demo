package com.todo.demo.component;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.TaskPriorityEnum;
import com.todo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.todo.demo.component.StringUtils.capitalizeWords;

@Component
public class Choice {

    private final UserService userService;
    private final Scanner scanner;
    private final SupplierComponent supplierComponent;

    @Autowired
    public Choice(UserService userService, Scanner scanner, SupplierComponent supplierComponent) {
        this.userService = userService;
        this.scanner = scanner;
        this.supplierComponent = supplierComponent;
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
//        for (int i = 0; i < userList.size(); i++) System.out.println(i + 1 + ". " + userList.get(i).getName());

        Map<Integer, UserDTO> userMap = IntStream.range(0, userList.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i+1,
                        userList::get
                ));
        System.out.println("Please enter the assignee name/number: ");

        userMap.forEach((number, user) -> System.out.println(number + ". " + user.getName()));


        return supplierComponent.safePrompt(() -> {
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
