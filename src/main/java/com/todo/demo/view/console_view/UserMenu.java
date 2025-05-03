package com.todo.demo.view.console_view;


import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.enums.UserMenuEnum;
import com.todo.demo.model.exception.NotFoundException;
import com.todo.demo.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class UserMenu {

    private final UserService userService;
    private final Scanner scanner;

    public UserMenu(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    public void userMenu(UserDTO user) {
        String choice;

        while (true) {
            System.out.println("User menu:");
            for (UserMenuEnum menu : UserMenuEnum.values()) {
                System.out.println(menu);
            }
            choice = scanner.nextLine();
            for (UserMenuEnum menu : UserMenuEnum.values()) {
                if (menu.toString().equalsIgnoreCase(choice)) {
                    switch (menu) {
                        case READ ->
                            //todo.. протестировать поведение вывода модели, а не поля
                            System.out.printf("""
                                    Current user:
                                    Id: %d
                                    Name: %s
                                    %n""", user.getId(), user.getName());


                        case UPDATE -> {
                            UserUpdateDTO updateDTO = new UserUpdateDTO();
                            updateDTO.setId(user.getId());
                            System.out.println("""
                                    Update:
                                    1. Name
                                    2. Password
                                    3. Go back
                                    """);
                            choice = scanner.nextLine();
                            switch (choice.toLowerCase()) {
                                case "name", "1" -> {
                                    System.out.println("Enter new name:");
                                    String newName = scanner.nextLine();
                                    if (newName == null || newName.trim().isEmpty()) {
                                        System.out.println("Name cannot be empty");
                                        break;
                                    }
                                    updateDTO.setName(Optional.of(newName));
                                    Optional<UserDTO> updatedUser = userService.updateUser(updateDTO);
                                    if (updatedUser.isPresent()) {
                                        System.out.println("User updated");
                                        user = updatedUser.get();
                                    } else {
                                        System.out.println("User not updated");
                                    }
                                }
                                case "password", "2" -> {
                                    System.out.println("Enter old password:");

                                    try {
                                        if (userService.checkPassword(scanner.nextLine(), user.getId())) {
                                            System.out.println("Enter new password:");
                                            String newPassword = scanner.nextLine();
                                            System.out.println("Enter new password again:");
                                            if (newPassword.equals(scanner.nextLine())) {
                                                updateDTO.setPassword(Optional.of(newPassword));
                                                userService.updateUser(updateDTO);
                                            } else System.out.println("Passwords don't match");
                                        } else System.out.println("Wrong password!");
                                    } catch (NotFoundException e) {
                                        System.out.println("User not found");
                                    }
                                }
                                case "back", "3" -> {
                                    return;
                                }

                                default -> System.out.println("???");
                            }
                        }

                        case DELETE -> {
                            System.out.println("Are you sure you want to delete the user?(Yes/No):");

                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                userService.deleteUser(user.getId());
                            }
                            return;
                        }

                        case BACK -> {
                            return;
                        }
                        default -> System.out.println("???");
                    }
                }
            }
        }
    }
}
