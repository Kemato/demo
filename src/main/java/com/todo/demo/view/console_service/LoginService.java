package com.todo.demo.view.console_service;

import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private final UserService userService;
    private final Scanner scanner;
    private final Validator validator;
    private final SafePromtService safePromtService;

    @Autowired
    public LoginService(UserService userService, Scanner scanner, Validator validator, SafePromtService safePromtService) {
        this.userService = userService;
        this.scanner = scanner;
        this.validator = validator;
        this.safePromtService = safePromtService;
    }

    public UserDTO processCommand(String command) {
        switch (command) {
            case "1", "login", "log", "l" -> {
                UserCredentials credentials = promptCredentials();
                try {
                    return userService.login(credentials.name(), credentials.password());
                } catch (IllegalArgumentException e) {
                    logger.warn("Login failed: {}", e.getMessage(), e);
                    System.out.println("Invalid username or password.");
                    return null;
                }
            }
            case "2", "register", "reg", "r" -> {
                UserCredentials credentials = promptCredentials();
                System.out.print("Enter Password again: ");
                String confirmPassword = scanner.nextLine();
                if (!credentials.password().equals(confirmPassword)) {
                    System.out.println("Passwords do not match.");
                    return null;
                }
                UserCreateDTO userCreateDTO = new UserCreateDTO(credentials.name(), credentials.password());
                Set<ConstraintViolation<UserCreateDTO>> violations = validator.validate(userCreateDTO);
                if (!violations.isEmpty()) {
                    for (ConstraintViolation<UserCreateDTO> violation : violations) {
                        System.out.println("Ошибка: " + violation.getMessage());
                    }
                    return null;
                }
                try {
                    return userService.createUser(userCreateDTO);
                } catch (IllegalArgumentException e) {
                    logger.warn("Registration failed: {}", e.getMessage(), e);
                    System.out.println("Registration failed: " + e.getMessage());
                    return null;
                }

            }
            case "0", "exit" -> {
                System.out.println("Exiting...");
                return null;
            }
            default -> {
                System.out.println("Invalid choice. Please try again.");
                return null;
            }
        }
    }

    private record UserCredentials(String name, String password) {
    }

    private UserCredentials promptCredentials() {
        return safePromtService.safePrompt(() -> {
            System.out.print("Enter Username: ");
            String name = scanner.nextLine();
            if (name.isBlank() || name.length() < 3) {
                throw new IllegalArgumentException("Username must contain at least 3 characters");
            }

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            if (password.isBlank()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            return new UserCredentials(name, password);
        });
    }
}