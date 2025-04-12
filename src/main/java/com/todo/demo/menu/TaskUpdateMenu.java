package com.todo.demo.menu;

import ru.todo.model.MenuUpdateTask;
import ru.todo.model.TaskPriority;
import ru.todo.model.TaskStatus;
import ru.todo.service.TaskService;

import java.util.Scanner;

import static ru.todo.service.CapitalizeWords.capitalizeWords;

public class TaskUpdateMenu {
    public static void taskUpdateMenu(int id, TaskService taskService) {
        Scanner sc = new Scanner(System.in);

        boolean flag = true;
        String choice;
        while (flag) {
            System.out.println("Выберете поле, которое вы хотите обновить:");
            for (MenuUpdateTask point : MenuUpdateTask.values()) {
                System.out.println(capitalizeWords(point.toString().toLowerCase()));
            }
            choice = sc.nextLine();
            for (MenuUpdateTask point : MenuUpdateTask.values()) {
                if (point.toString().equalsIgnoreCase(choice)) {
                    flag = false;
                    switch (point) {
                        case NAME:
                            System.out.println("Введите новое имя:");
                            String newName = sc.nextLine();
                            if (taskService.changeTaskName(id, newName)) System.out.println("Изменения применены.");
                            else System.out.println("Что-то пошло не так. ");
                            return;

                        case DESCRIPTION:
                            System.out.println("Введите новое описание:");
                            String newDescription = sc.nextLine();
                            taskService.changeTaskDescription(id, newDescription);
                            return;

                        case ASSIGNED:
                            System.out.println("Кому назначить:");
                            //todo.. Нужно вывести текущих пользователей, и проверить соответствие введённого варианта
                            String newAssigned = sc.nextLine();
                            taskService.changeTaskName(id, newAssigned);
                            return;

                        case STATUS:
                            String newStatus = "";
                            boolean flagStatus = true;
                            while (flagStatus) {
                                for (TaskStatus status : TaskStatus.values()) {
                                    System.out.println(capitalizeWords(status.toString().toLowerCase()));
                                }
                                System.out.println("Введите новый статус:");
                                newStatus = sc.nextLine();
                                for (TaskStatus status : TaskStatus.values()) {
                                    if (status.toString().equalsIgnoreCase(newStatus)) {
                                        newStatus = status.toString();
                                        flagStatus = false;
                                        break;
                                    }
                                }
                                System.out.println("Некорректный статус.");
                            }
                            taskService.changeTaskStatus(id, newStatus);
                            return;

                        case PRIORITY:
                            String newPriority = "";
                            boolean flagPriority = true;
                            while (flagPriority) {
                                for (TaskPriority priority : TaskPriority.values()) {
                                    System.out.println(capitalizeWords(priority.toString().toLowerCase()));
                                }
                                System.out.println("Введите приоритет:");
                                newPriority = sc.nextLine();
                                for (TaskPriority priority : TaskPriority.values()) {
                                    if (priority.toString().equalsIgnoreCase(newPriority)) {
                                        newPriority = priority.toString();
                                        flagPriority = false;
                                        break;
                                    }
                                }
                                System.out.println("Некорректный статус.");
                            }
                            taskService.changeTaskPriority(id, newPriority);
                            return;
                        default:
                            System.out.println("Некорректный ввод.");
                            return;
                    }
                }
            }
            if (flag) System.out.println("Некорректный ввод.");
        }
    }
}
