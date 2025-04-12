package com.todo.demo.menu;


import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.enums.UserMenuEnum;
import com.todo.demo.services.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class UserMenu {

    private final UserService userService;

    public UserMenu(UserService userService) {
        this.userService = userService;
    }

    public void userMenu(UserDTO user) {
        String choice;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("User menu:");
            for (UserMenuEnum menu : UserMenuEnum.values()) {
                System.out.println(menu);
            }
            choice = scanner.nextLine();
            for (UserMenuEnum menu : UserMenuEnum.values()) {
                if (menu.toString().equalsIgnoreCase(choice)) {
                    switch (menu) {
                        case READ:
                            //todo.. протестировать поведение вывода модели, а не поля
                            System.out.println("Current user " + user);

                        case UPDATE:
                            UserUpdateDTO updateDTO = new UserUpdateDTO();
                            updateDTO.setId(user.getId());
                            System.out.println("1.Update name/password?");
                            System.out.println("To go back input *back*");
                            choice = scanner.nextLine();
                            switch (choice) {
                                case "name":
                                    System.out.println("Enter new name:");
                                    String newName = scanner.nextLine();
                                    if (newName == null || newName.trim().isEmpty()) {
                                        System.out.println("Name cannot be empty");
                                        break;
                                    }
                                    updateDTO.setName(Optional.of(scanner.nextLine()));
                                    user = userService.updateUser(updateDTO);
                                    break;
                                case "password":
                                    System.out.println("Enter old password:");
                                    if (userService.checkPassword(scanner.nextLine(), user.getId())) {
                                        System.out.println("Enter new password:");
                                        String newPassword = scanner.nextLine();
                                        System.out.println("Enter new password again:");
                                        if (newPassword.equals(scanner.nextLine())){
                                            updateDTO.setPassword(Optional.of(newPassword));
                                            userService.updateUser(updateDTO);
                                        }
                                        else System.out.println("Passwords don't match");
                                    }
                                    else System.out.println("Wrong password!");
                                    break;
                                case "back":
                                    return;
                                default:
                                    System.out.println("???");
                            }
                            break;

                        case DELETE:
                            System.out.println("Are you sure you want to delete the user?(Yes/No):");
                            if(scanner.nextLine().equalsIgnoreCase("yes")) {
                                userService.deleteUser(user.getId());
                            }
                            return;

                        case BACK:
                            return;

                        default:
                            System.out.println("???");
                            break;
                    }
                }
            }
        }
    }
}
