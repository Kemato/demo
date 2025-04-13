package com.todo.demo.menu;

import com.todo.demo.model.dto.TaskCreateDTO;
import com.todo.demo.model.enums.TaskMenuEnum;
import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.services.TaskService;
import com.todo.demo.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        ArrayList<TaskDTO> allTasksByUser = taskService.readAllTaskByUser(user.getId());
        ArrayList<TaskDTO> allTasks = taskService.readAllTasks();
        String choice;
        while (true) {
            if(allTasks.isEmpty()) {
                System.out.println(TaskMenuEnum.CREATE);
            }
            else {
                for (TaskMenuEnum taskMenuEnum : TaskMenuEnum.values()) System.out.println(taskMenuEnum);
            }
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
                            if (allTasks.isEmpty()) {
                                System.out.println("There are no tasks.");
                                break;
                            }
                            System.out.println("1:Все задачи.");
                            System.out.println("2:Только мои задачи.");
                            String input = scanner.nextLine();
                            if (input.equals("1")) {
                                for (TaskDTO task : allTasks) {
                                    printTask(task);
                                }
                            } else if (input.equals("2")) {
                                for (TaskDTO task : allTasksByUser) {
                                    printTask(task);
                                }
                            }
                            else{
                                System.out.println("Некорректный ввод");
                            }
                            System.out.println("Введите любой символ чтобы продолжить...");
                            scanner.nextLine();
                            break;
                        case UPDATE:
                            ArrayList<TaskDTO> taskDtoArrayWhereAuthor = taskService.readAllTaskByAuthor(user.getId());
                            if (taskDtoArrayWhereAuthor.isEmpty()) {
                                System.out.println("You have no tasks that you can update.");
                                break;
                            }
                            System.out.println("Введите порядковый номер задания, которое вы хотите обновить");
                            for (int i = 0; i < taskDtoArrayWhereAuthor.size(); i++) {
                                System.out.println(i+1 + ":");
                                printTask(taskDtoArrayWhereAuthor.get(i));
                            }
                            while (true) {
                                choice = scanner.nextLine();
                                if (Long.parseLong(choice) <= taskDtoArrayWhereAuthor.size() && Long.parseLong(choice) > 0) {
                                    break;
                                }
                                System.out.println("Выберете существующее задание.");
                            }
                            TaskUpdateMenu.taskUpdateMenu(Integer.parseInt(choice) - 1);
                            break;

                        case DELETE:
                            if (allTasks.isEmpty()) {
                                System.out.println("There are no tasks.");
                                break;
                            }
                            System.out.println("Введите порядковый номер записи, которую вы хотите удалить.");
                            Long id = Long.parseLong(scanner.nextLine());
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

    public void printTask(@NotNull TaskDTO task) {
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Author: " + userService.readUser(task.getAuthor()));
        System.out.println("Assigned: " + userService.readUser(task.getAssignee()));
        System.out.println("Status: " + task.getStatus());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Date Created: " + task.getDateCreated());
        System.out.println("Date Updated: " + task.getDateUpdated());
        System.out.println("Deadline: " + task.getDeadline());
        System.out.println("DeadFinished:" + task.getDateFinished());
    }


}
