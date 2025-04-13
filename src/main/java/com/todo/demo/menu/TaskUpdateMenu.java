package com.todo.demo.menu;



import com.todo.demo.model.dto.TaskUpdateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.MenuUpdateTask;
import com.todo.demo.model.enums.TaskPriorityEnum;
import com.todo.demo.model.enums.TaskStatusEnum;
import com.todo.demo.services.TaskService;
import com.todo.demo.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import static com.todo.demo.component.Choise.capitalizeWords;

@Component
public class TaskUpdateMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final TaskService taskService;
    private final UserService userService;

    public TaskUpdateMenu(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public void taskUpdateMenu(Long id) {
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
            for (MenuUpdateTask point : MenuUpdateTask.values()) {
                if (point.toString().equalsIgnoreCase(choice)) {
                    flag = false;
                    switch (point) {
                        case TITLE:
                            System.out.println("Введите новое имя:");
                            String newTitle = scanner.nextLine();
                            if(newTitle == null || newTitle.trim().isEmpty())
                                throw new IllegalArgumentException("Title cannot be empty");
                            taskUpdateDTO.setTitle(Optional.of(newTitle));
                            break;

                        case DESCRIPTION:
                            System.out.println("Введите новое описание:");
                            String newDescription = scanner.nextLine();
                            if(newDescription == null || newDescription.trim().isEmpty())
                                throw new IllegalArgumentException("Description cannot be empty");
                            taskUpdateDTO.setDescription(Optional.of(newDescription));
                            break;

                        case ASSIGNED:
                            ArrayList <UserDTO> userArrayList = userService.readAllUsers();
                            System.out.println("Кому назначить:");
                            for(UserDTO user : userArrayList)System.out.println(user.getName());
                            while(true) {
                                String newAssigned = scanner.nextLine();
                                if(newAssigned.equalsIgnoreCase("back"))break;
                                for (UserDTO user : userArrayList) {
                                    if (user.getName().equals(newAssigned)) {
                                        taskUpdateDTO.setAssigned(Optional.of(newAssigned));
                                        break;
                                    }
                                }
                                System.out.println("Некорректный ввод.");
                                System.out.println("Введите имя существующего пользователя или 'back' для того чтобы вернуться.");
                            }
                            break;

                        case STATUS:
                            String newStatus = "";
                            while (true) {
                                for (TaskStatusEnum status : TaskStatusEnum.values()) {
                                    System.out.println(capitalizeWords(status.toString().toLowerCase()));
                                }
                                System.out.println("Введите новый статус:");
                                newStatus = scanner.nextLine();
                                for (TaskStatusEnum status : TaskStatusEnum.values()) {
                                    if (status.toString().equalsIgnoreCase(newStatus)) {
                                        newStatus = status.toString();
                                        break;
                                    }
                                }
                                System.out.println("Некорректный статус.");
                            }
                            taskService.changeTaskStatus(id, newStatus);
                            break;

                        case PRIORITY:
                            String newPriority = "";
                            boolean flagPriority = true;
                            while (flagPriority) {
                                for (TaskPriorityEnum priority : TaskPriorityEnum.values()) {
                                    System.out.println(capitalizeWords(priority.toString().toLowerCase()));
                                }
                                System.out.println("Введите приоритет:");
                                newPriority = scanner.nextLine();
                                for (TaskPriorityEnum priority : TaskPriorityEnum.values()) {
                                    if (priority.toString().equalsIgnoreCase(newPriority)) {
                                        newPriority = priority.toString();
                                        flagPriority = false;
                                        break;
                                    }
                                }
                                System.out.println("Некорректный статус.");
                            }
                            taskService.changeTaskPriority(id, newPriority);
                            break;
                        default:
                            System.out.println("Некорректный ввод.");
                            break;
                    }
                }
            }
            if (flag) System.out.println("Некорректный ввод.");
        }
    }
}
