package com.todo.demo.view.console;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Console-based main menu for user interaction.
 */
@Component
public class MainMenu {
    private final MainService mainService;
    private final Scanner scanner;

    @Autowired
    public MainMenu(MainService mainService, Scanner scanner) {
        this.mainService = mainService;
        this.scanner = scanner;
    }

    /**
     * Displays the main menu and processes user input.
     * @param userDTO The authenticated user.
     */
    public void showMainMenu(UserDTO userDTO) {

            while (true) {
                System.out.flush();
                System.out.print("""
                        
                        Main Menu:
                        1. User Menu
                        2. Task Menu
                        0. Log Out
                        Enter choice:\s""");
                String choice = scanner.nextLine();
                if (choice == null || choice.trim().isEmpty()) {
                    System.out.println("Input cannot be empty.");
                    continue;
                }
                if (mainService.processCommand(choice.toLowerCase(), userDTO)) {
                    return; // Выход из меню
                }
            }
        }

}