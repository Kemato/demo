package com.todo.demo.view.console_view;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.view.console_service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginMenu {
    private final LoginService loginService;
    private final Scanner scanner;

    @Autowired
    public LoginMenu(LoginService loginService, Scanner scanner) {
        this.loginService = loginService;
        this.scanner = scanner;
    }

    public UserDTO showMenu() {

        while (true) {
            System.out.print("""
                    Choose:
                    1. Login
                    2. Register
                    0. Exit
                    """);
            String choice = scanner.nextLine();
            if (choice == null || choice.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
                continue;
            }
            UserDTO result = loginService.processCommand(choice.toLowerCase());
            if (result != null || choice.equals("0") || choice.equals("exit")) {
                return result;
            }
        }

    }
}