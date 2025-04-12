package com.todo.demo.parse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import ru.todo.model.Task;
import ru.todo.model.TaskList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class XmlTaskParse {
    private static final String USERTASKLIST_XML = "src/main/java/data/tasks.xml";

    public ArrayList<Task> read() {
        try {
            var context = JAXBContext.newInstance(TaskList.class);
            var um = context.createUnmarshaller();
            return (
                    ((TaskList)
                            um.unmarshal(
                                    new InputStreamReader(
                                            new FileInputStream
                                                    (USERTASKLIST_XML),
                                            StandardCharsets.UTF_8))
                    ).getTaskList());
        }
        catch (JAXBException | IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public void write(TaskList taskList) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(TaskList.class);
            Marshaller m = null;
            m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(taskList, new File(USERTASKLIST_XML));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }
}
