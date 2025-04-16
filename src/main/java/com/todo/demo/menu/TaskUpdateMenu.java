package com.todo.demo.menu;

import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.MenuUpdateTask;
import com.todo.demo.model.enums.TaskPriorityEnum;
import com.todo.demo.model.enums.TaskStatusEnum;
import com.todo.demo.services.TaskService;
import com.todo.demo.services.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import static com.todo.demo.component.Choise.capitalizeWords;

@Component
public class TaskUpdateMenu {

    private final TaskService taskService;
    private final UserService userService;

    public TaskUpdateMenu(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public static void taskUpdateMenu(Long id) {
        if (id == null) {
            System.out.println("Task ID cannot be null.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
            taskUpdateDTO.setId(id);
            boolean flag = true;
            String choice;

            while (flag) {
                System.out.println("Выберете поле, которое вы хотите обновить:");
                for (MenuUpdateTask point : MenuUpdateTask.values()) {
                    System.out.println(capitalizeWords(point.toString().toLowerCase()));
                }
                choice = scanner.nextLine();

                try {
                    MenuUpdateTask point = MenuUpdateTask.valueOf(choice.toUpperCase());
                    switch (point) {
                        case TITLE:
                            while (true) {
                                System.out.println("Введите новое имя:");
                                String newTitle = scanner.nextLine();
                                if (newTitle == null || newTitle.trim().isEmpty()) {
                                    System.out.println("Title cannot be empty. Try again.");
                                    continue;
                                }
                                taskUpdateDTO.setTitle(Optional.of(newTitle));
                                break;
                            }
                            break;

                        case DESCRIPTION:
                            while (true) {
                                System.out.println("Введите новое описание:");
                                String newDescription = scanner.nextLine();
                                if (newDescription == null || newDescription.trim().isEmpty()) {
                                    System.out.println("Description cannot be empty. Try again.");
                                    continue;
                                }
                                taskUpdateDTO.setDescription(Optional.of(newDescription));
                                break;
                            }
                            break;

                        case ASSIGNED:
                            ArrayList<UserDTO> userArrayList = userService.readAllUsers();
                            System.out.println("Кому назначить:");
                            for (UserDTO user : userArrayList) System.out.println(user.getName());
                            while (true) {
                                String newAssigned = scanner.nextLine();
                                if (newAssigned.equalsIgnoreCase("back")) break;
                                if (userArrayList.stream().anyMatch(user -> user.getName().equals(newAssigned))) {
                                    taskUpdateDTO.setAssigned(Optional.of(newAssigned));
                                    break;
                                }
                                System.out.println("Некорректный ввод.");
                                System.out.println("Введите имя существующего пользователя или 'back', чтобы вернуться.");
                            }
                            break;

                        case STATUS:
                            for (TaskStatusEnum status : TaskStatusEnum.values()) {
                                System.out.println(capitalizeWords(status.toString().toLowerCase()));
                            }
                            while (true) {
                                System.out.println("Какой статус назначить: ");
                                String newStatus = scanner.nextLine();
                                if (newStatus.equalsIgnoreCase("back")) break;
                                try {
                                    TaskStatusEnum.valueOf(newStatus.toUpperCase());
                                    taskUpdateDTO.setStatus(Optional.of(newStatus));
                                    break;
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Некорректный ввод. Попробуйте снова, или введите 'back', чтобы вернуться.");
                                }
                            }
                            break;

                        case PRIORITY:
                            for (TaskPriorityEnum priority : TaskPriorityEnum.values()) {
                                System.out.println(capitalizeWords(priority.toString().toLowerCase()));
                            }
                            while (true) {
                                String newPriority = scanner.nextLine();
                                if (newPriority.equalsIgnoreCase("back")) break;
                                try {
                                    TaskPriorityEnum.valueOf(newPriority.toUpperCase());
                                    taskUpdateDTO.setPriority(Optional.of(newPriority));
                                    break;
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Некорректный ввод. Попробуйте снова, или введите 'back', чтобы вернуться.");
                                }
                            }
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Некорректный ввод.");
                }

                do {
                    System.out.println("Хотите изменить что-нибудь ещё? (yes/no)");
                    choice = scanner.nextLine().toLowerCase();
                    if (!choice.equals("yes") && !choice.equals("no")) {
                        System.out.println("Некорректный ввод");
                    }
                } while (!choice.equals("yes") && !choice.equals("no"));
                flag = choice.equals("yes");
            }

            if (taskUpdateDTO.getTitle().isPresent() || taskUpdateDTO.getDescription().isPresent() ||
                    taskUpdateDTO.getAssigned().isPresent() || taskUpdateDTO.getStatus().isPresent() ||
                    taskUpdateDTO.getPriority().isPresent()) {
                taskService.updateTask(taskUpdateDTO);
                System.out.println("Task updated successfully.");
            }
        }
    }
}