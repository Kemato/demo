package com.todo.demo.menu;


import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.enums.MainMenuEnum;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Scanner;

@Component
public class MainMenu {

    private final UserMenu userMenu;
    private final TaskMenu taskMenu;

    public MainMenu(UserMenu userMenu, TaskMenu taskMenu) {
        this.userMenu = userMenu;
        this.taskMenu = taskMenu;
    }

    public void mainMenu(UserDTO userDTO) {
        Scanner sc = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.println("Main menu.");
            for (MainMenuEnum mainMenu : MainMenuEnum.values()) {
                System.out.println(mainMenu);
            }
            choice = sc.nextLine();
            for (MainMenuEnum mainMenu : MainMenuEnum.values()) {
                if (mainMenu.toString().equalsIgnoreCase(choice)) {
                    switch (mainMenu) {
                        case USER_MENU:
                            userMenu.userMenu(userDTO);
                            break;
                        case TASK_MENU:
                            taskMenu(userDTO);
                            break;
                        case LOG_OUT:
                            return;
                    }
                }
            }
        }
    }
}
