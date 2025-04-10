package com.todo.demo.interfaces;

import com.todo.demo.model.UserEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository <UserEntity, Long>{

    //Кастомный запрос
    @Query("SELECT * FROM users WHERE name=:name")
    Optional<UserEntity> findByName(String name);

    boolean existsByName(String username);
}
