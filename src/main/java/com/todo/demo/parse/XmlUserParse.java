package com.todo.demo.parse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import ru.todo.model.User;
import ru.todo.model.UserList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class XmlUserParse {
    private static final String USERLIST_XML = "src/main/java/data/users.xml";

    public ArrayList<User> read() {
        try {
            var context = JAXBContext.newInstance(UserList.class);
            var um = context.createUnmarshaller();
            return (
                    ((UserList)
                            um.unmarshal(
                                    new InputStreamReader(
                                            new FileInputStream(USERLIST_XML), StandardCharsets.UTF_8))).getUserList());
        }
        catch(IOException | JAXBException e){
            throw new RuntimeException(e);
        }
    }

    public void write() {
        try {
            var context = JAXBContext.newInstance(UserList.class);
            var m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(UserList.getInstance(), new File(USERLIST_XML));
        }
        catch(JAXBException e){
            throw new RuntimeException(e);
        }
    }
}
