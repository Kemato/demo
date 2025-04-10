package com.todo.demo.exeption;

public class TaskExeption extends RuntimeException {
    public TaskExeption(String message) {
        super(message);
    }
}
