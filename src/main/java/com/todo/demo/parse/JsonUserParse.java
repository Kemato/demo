package com.todo.demo.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.todo.model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class JsonUserParse {
    private static final String USERLIST_JSON = "src/main/java/data/users.json";

    public ArrayList <User> read() {
        try (FileReader reader = new FileReader(USERLIST_JSON);) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
        } catch (Exception e) {
            System.out.println("Parsing user_list_json error " + e.getMessage());
        }
        return null;
    }

    public void write(ArrayList<User> users) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(USERLIST_JSON)) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            writer.write(mapper.writeValueAsString(users));
        } catch (Exception e) {
            System.out.println("Writing user_list_json error " + e.getMessage());
        }
    }
}
