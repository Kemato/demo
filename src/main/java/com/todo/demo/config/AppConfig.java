package com.todo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Configuration
public class AppConfig {

    @Bean
    public Scanner scanner(){
        Reader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        return new Scanner(reader);
    }
}
