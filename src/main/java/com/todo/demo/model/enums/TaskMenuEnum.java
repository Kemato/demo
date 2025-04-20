package com.todo.demo.model.enums;

import java.util.Arrays;

public enum TaskMenuEnum {
    CREATE("1", "create", "new"),
    READ("2", "read", "list"),
    UPDATE("3", "update", "edit"),
    DELETE("4", "delete", "remove"),
    BACK("0", "back", "exit");

    private final String[] aliases;

    TaskMenuEnum(String... aliases) {
        this.aliases = aliases;
    }

    public static TaskMenuEnum fromString(String input) {
        for (TaskMenuEnum menu : values()) {
            if (Arrays.asList(menu.aliases).contains(input.toLowerCase())) {
                return menu;
            }
        }
        return null;
    }
}