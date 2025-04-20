package com.todo.demo.component;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : input.trim().toCharArray()) {
            result.append(capitalizeNext ? Character.toUpperCase(c) : c);
            capitalizeNext = c == ' ';
        }
        return result.toString();
    }
}