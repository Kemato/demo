package com.todo.demo.view.console_service;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class SafePromtService {
    public <T> T safePrompt(Supplier<T> action) {
        while (true) {
            try {
                return action.get();
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Пожалуйста, попробуйте ещё раз.\n");
            }
        }
    }
}
