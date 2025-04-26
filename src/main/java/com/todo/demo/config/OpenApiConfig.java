package com.todo.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Todo Application API",
                version = "1.0",
                description = "API documentation for the Todo application"
        )
)
public class OpenApiConfig {
}
