package com.todo.demo.component;

import com.todo.demo.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class SupplierComponent {
    private final Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    SupplierComponent( Validator validator){
        this.validator = validator;
    }

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
    public <T> T handleDatabaseOperation(Supplier<T> operation, String errorMessage) {
        try {
            return operation.get();
        } catch (DataAccessException e) {
            logger.error("{}: {}", errorMessage, e.getMessage(), e);
            throw new RuntimeException(errorMessage + ": " + e.getMessage(), e);
        }
    }

    public <T> void validateDto(T dto) {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + "; " + m2)
                    .orElse("Validation error");
            logger.warn("Validation failed: {}", errorMessages);
            throw new IllegalArgumentException(errorMessages);
        }
    }
}
