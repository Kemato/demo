package com.todo.demo.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.todo.demo.model.dto.TaskDTO

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;


public class JsonTaskParse {
    private static final String USERTASKLIST_JSON = "src/main/java/data/tasks.json";

    public ArrayList <TaskDTO> read() {
        try (FileReader reader = new FileReader(USERTASKLIST_JSON)) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    reader,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, TaskDTO.class)
            );
        } catch (Exception e) {
            System.out.println("Read task_list_json Error " + e.getMessage());
        }
        return null;
    }

    public void write(ArrayList <TaskDTO> tasks) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(USERTASKLIST_JSON)) {
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            writer.write(mapper.writeValueAsString(tasks));
        } catch (Exception e) {
            System.out.println("Writing task_list_json Error " + e.getMessage());
        }
    }
}
