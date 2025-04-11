package com.todo.demo.services;

import com.todo.demo.interfaces.UserMapper;
import com.todo.demo.interfaces.UserRepository;
import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserEntity createUser(String username, String password){
        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if(password == null || password.trim().isEmpty()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        Optional <UserEntity> existingUser = userRepository.findByName(username);
        if(existingUser.isPresent()){
            throw new IllegalArgumentException("User with name "+ username +" already exists");
        }
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName(username);
        userCreateDTO.setPassword(passwordEncoder.encode(password));
        userRepository.save(userMapper.map(userCreateDTO));
        return userRepository.findByName(username).orElse(null);
    }

    public Optional<UserEntity> readUserByUsername(String username){
        try {
            return userRepository.findByName(username);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Error reading user " + e.getMessage(), e);
        }
    }

    public UserEntity updateUser(UserUpdateDTO userUpdateDTO){
        try {
            if (userUpdateDTO.getId() == null || !userRepository.existsById(userEntity.getId())) {
                throw new IllegalArgumentException("User not found ");
            }
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(userEntity);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Could not update user "+ e.getMessage(), e);
        }
    }

//    public UserEntity updateUserPassword(UserEntity userEntity, String newPassword){
//        try {
//            if (userEntity.getId() == null || !userRepository.existsById(userEntity.getId())) {
//                throw new IllegalArgumentException("User not found ");
//            }
//            userEntity.setPassword(passwordEncoder.encode(newPassword));
//            return userRepository.save(userEntity);
//        }
//        catch (DataAccessException e){
//            throw new RuntimeException("Could not update user "+ e.getMessage(), e);
//        }
//    }
//
//    public UserEntity updateUserName(UserEntity userEntity, String username){
//        try {
//            if (userEntity.getId() == null || !userRepository.existsById(userEntity.getId())) {
//                throw new IllegalArgumentException("User not found ");
//            }
//            userEntity.setName(username);
//            return userRepository.save(userEntity);
//        }
//        catch (DataAccessException e){
//            throw new RuntimeException("Could not update user "+ e.getMessage(), e);
//        }
//    }

    public boolean deleteUser(UserEntity userEntity){
        try {
            if (userEntity.getId() == null || !userRepository.existsById(userEntity.getId())) {
                throw new IllegalArgumentException("User not found");
            } else {
                userRepository.deleteById(userEntity.getId());
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

    public ArrayList <UserEntity> readAllUsers(){
        try{
            ArrayList<UserEntity> userEntities = new ArrayList<>();
            userRepository.findAll().forEach(userEntities::add);
            if(userEntities.isEmpty()){
                throw new IllegalArgumentException("No users found");
            }
            return userEntities;
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding users " + ex.getMessage(), ex);
        }
    }

    public UserEntity readUser(Long id){
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
