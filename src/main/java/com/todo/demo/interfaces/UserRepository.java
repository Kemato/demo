package com.todo.demo.interfaces;

import com.todo.demo.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository <User, Long>{

    //Кастомный запрос
    @Query("SELECT * FROM users WHERE name=:name")
    Optional<User> findByName(String name);

    boolean existsByName(String username);
}
