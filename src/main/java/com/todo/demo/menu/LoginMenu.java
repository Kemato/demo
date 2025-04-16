package com.todo.demo.menu;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.services.UserService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LoginMenu {
    private final UserService userService;

    LoginMenu(final UserService userService) {
        this.userService = userService;
    }

    public UserDTO user loginMenu(){
        Scanner sc = new Scanner(System.in);
        boolean loggedIn = false;
        while(!loggedIn) {
            System.out.format("Choose:\n1.Login\n2.Register\n");
            String name = null, password, choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Enter username: ");
                    name = sc.nextLine();
                    System.out.println("Enter password: ");
                    //todo.. посмотреть, как скрыть символы, во время ввода пароля
                    password = sha256hex(sc.nextLine());
                    userService.login(name, password);
                    if(userService.readUser() != null) {
                        System.out.println("Welcome back " + userService.readUser().getName() + "!");
                        loggedIn = true;
                        mainMenu();
                    }
                    System.out.println("Login failed!");
                    break;
                case "2":
                    while(name == null) {
                        System.out.println("Enter username: ");
                        name = sc.nextLine();
                        for(User user : userService.getUserList()) {
                            if(user.getName().equals(name)) {
                                System.out.println("This name is already in use!");
                                name = null;
                                break;
                            }
                        }
                    }
                    System.out.println("Enter password: ");
                    //todo.. посмотреть, как скрыть символы, во время ввода пароля
                    password = sha256hex(sc.nextLine());
                    userService.createUser(name, password);
                    //todo.. Добавить функционал, чтобы имена пользователей не повторялись
                    System.out.println("Welcome " + userService.readUser().getName() + "!");
                    loggedIn = true;
                    break;
                default:
                    System.out.println("Try again please.");
                    loggedIn = false;
                    break;
            }
        }
    }
}
