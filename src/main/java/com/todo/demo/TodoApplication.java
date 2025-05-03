package com.todo.demo;

import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.view.console_view.LoginMenu;
import com.todo.demo.view.console_view.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication implements CommandLineRunner {
	public final MainMenu mainMenu;
	public final LoginMenu loginMenu;

	public TodoApplication(MainMenu mainMenu, LoginMenu loginmenu){
		this.mainMenu = mainMenu;
		this.loginMenu = loginmenu;
	}
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Override
	public void run(String... args){
		while(true){
			UserDTO user = loginMenu.showMenu();
			if (user != null) {
				// Если логин успешен, показываем основное меню
				mainMenu.showMainMenu(user);
			} else {

				System.out.println("Login failed. Exiting application.");
			}
		}
	}
}
