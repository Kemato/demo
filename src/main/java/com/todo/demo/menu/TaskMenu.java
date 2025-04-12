package com.todo.demo.menu;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.enums.TaskMenuEnum;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.services.TaskService;
import com.todo.demo.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static com.todo.demo.component.Choise.choiceAssegned;
import static com.todo.demo.component.Choise.choicePriority;


@Component
public class TaskMenu {
    private final TaskService taskService;
    private final UserService userService;

    public TaskMenu(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public void taskMenu(UserDTO user) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ArrayList<TaskDTO> tasks = taskService.readAllTasks();
        String name, description, priority, choice, assegned;
        while (true) {
            for (TaskMenuEnum taskMenuEnum : TaskMenuEnum.values()) System.out.println(taskMenuEnum);
            choice = scanner.nextLine();
            for (TaskMenuEnum taskMenuEnum : TaskMenuEnum.values()) {
                if (taskMenuEnum.toString().equalsIgnoreCase(choice)) {
                    switch (taskMenuEnum) {
                        case CREATE:
                            TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
                            System.out.println("Введите название: ");
                            taskCreateDTO.setTitle(scanner.nextLine());
                            System.out.println("Введите описание: ");
                            taskCreateDTO.setDescription(scanner.nextLine());
                            taskCreateDTO.setAssignee(choiceAssegned(userService.readAllUsers()));
                            taskCreateDTO.setPriority(choicePriority());
                            while (true) {
                                System.out.println("Введите дедлайн в формате 'dd/MM/yyyy': ");
                                try {
                                    LocalDate deadline = LocalDate.parse(scanner.nextLine(), formatter);
                                    if (deadline.isAfter(LocalDate.now())) break;
                                    else System.out.println("He won't have time");
                                } catch (RuntimeException e) {
                                    throw new IllegalArgumentException("Uncorrected type date" + e.getMessage(), e);
                                }
                            }

                            taskService.createTask(taskCreateDTO);
                            System.out.println("Новое задание создано!");
                            break;

                        case READ:
                            if (tasks.isEmpty()) {
                                System.out.println("There are no tasks.");
                                break;
                            }
                            System.out.println(
                                    "Введите id(1-" +
                                            tasks.size() +
                                            ")задания, если хотите вывести весь список, введите '-'."
                            );
                            String input = scanner.nextLine();
                            if (input.equals("-")) {
                                for (Task task : tasks) {
                                    printTask(task);
                                }
                            } else if (Integer.parseInt(input) > 0 && Integer.parseInt(input) < tasks.size() - 1) {
                                printTask(taskService.readTask(Integer.parseInt(input) - 1));
                            }
                            System.out.println("Введите любой символ чтобы продолжить...");
                            scanner.nextLine();
                            break;
                        case UPDATE:
                            if (tasks.isEmpty()) {
                                System.out.println("There are no tasks.");
                                break;
                            }
                            System.out.println("Введите порядковый номер задания, которое вы хотите исправить");
                            for (Task task : tasks) {
                                System.out.println(task.getName());
                                System.out.println(task.getDescription());
                                System.out.println("id: " + (task.getId() + 1));
                                System.out.println();
                            }
                            while (true) {
                                choice = scanner.nextLine();
                                if (Integer.parseInt(choice) <= tasks.size() && Integer.parseInt(choice) > 0) {
                                    break;
                                }
                                System.out.println("Выберете существующее задание.");
                            }
                            taskUpdateMenu(Integer.parseInt(choice) - 1, taskService);
                            break;

                        case DELETE:
                            if (tasks.isEmpty()) {
                                System.out.println("There are no tasks.");
                                break;
                            }
                            System.out.println("Введите порядковый номер записи, которую вы хотите удалить.");
                            int id = Integer.parseInt(scanner.nextLine());
                            System.out.println("Вы уверены что хотите удалить эту запись?(Yes/No)");
                            System.out.println(taskService.readTask(id).getName());

                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                if (taskService.deleteTask(id)) System.out.println("Успешно удалено.");
                            } else System.out.println("Удаление отменено.");
                            break;

                        case BACK:
                            return;

                        default:
                            System.out.print("???");
                            break;
                    }
                }
            }
            for (int i = 0; i < 6; ++i) System.out.println();
        }
    }

    public static void printTask(@NotNull Task task) {
        System.out.println("Name: " + task.getName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Author: " + task.getAuthor());
        System.out.println("Assigned: " + task.getAssigned());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Date Created: " + task.getDateCreated());
        System.out.println("Date Updated: " + task.getDateUpdated());
        System.out.println("Deadline: " + task.getDeadline());
        System.out.println("DeadFinished:" + task.getDateFinished());
    }


}
