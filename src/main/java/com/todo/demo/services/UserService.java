package com.todo.demo.services;

import com.todo.demo.interfaces.UserRepository;
import com.todo.demo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password){
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        Optional <User> existingUser = userRepository.findByName(username);
        if(existingUser.isPresent()){
            throw new IllegalArgumentException("User with name "+ username +" already exists");
        }
        User user = new User();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return userRepository.findByName(username).orElse(null);
    }

    public Optional<User> readUserByUsername(String username){
        try {
            return userRepository.findByName(username);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Error reading user " + e.getMessage(), e);
        }
    }

    public User updateUser(User user){
        try {
            if (user.getId() == null || !userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("User not found ");
            }
            return userRepository.save(user);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Could not update user "+ e.getMessage(), e);
        }
    }

    public boolean deleteUser(User user){
        try {
            if (user.getId() == null || !userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("User not found");
            } else {
                userRepository.deleteById(user.getId());
                return true;
            }
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while deleting user " + ex.getMessage(), ex);
        }
    }

    public boolean deleteUser(Long id){
        try{
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return true;
            }
            else{
                throw new IllegalArgumentException("User not found");
            }
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while deleting user " + ex.getMessage(), ex);
        }
    }

    public ArrayList <User> readAllUsers(){
        try{
            ArrayList<User> users = new ArrayList<>();
            userRepository.findAll().forEach(users::add);
            if(users.isEmpty()){
                throw new IllegalArgumentException("No users found");
            }
            return users;
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding users " + ex.getMessage(), ex);
        }
    }

    public User readUser(Long id){
        try{
            return userRepository.findById(id).orElse(null);
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }

    public boolean existsUser(Long id){
        try {
            return userRepository.existsById(id);
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }

    public boolean existsUser(String username){
        try{
            return userRepository.existsByName(username);
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }
}
