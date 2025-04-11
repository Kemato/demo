package com.todo.demo.services;

import com.todo.demo.interfaces.UserMapper;
import com.todo.demo.interfaces.UserRepository;
import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
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

    public UserDTO createUser(@NotNull UserCreateDTO userCreateDTO) {
        try {
            if (userCreateDTO.getName() == null || userCreateDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            Optional<UserEntity> existingUser = userRepository.findByName(userCreateDTO.getName());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("User with name " + userCreateDTO.getName() + " already exists");
            }
            UserEntity userEntity = userMapper.toEntity(userCreateDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            return userMapper.toDTO(userRepository.save(userEntity));
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Error creating user" +  e.getMessage(), e);
        }
    }

    public UserDTO readUser(@NotNull String username){
        try {
            Optional<UserEntity> userEntity = userRepository.findByName(username);
            if (userEntity.isPresent()) {
                return userMapper.toDTO(userEntity.get());
            }
            else {
                throw new IllegalArgumentException("User not found");
            }
        }
        catch (DataAccessException e){
            throw new RuntimeException("Error reading user " + e.getMessage(), e);
        }
    }

    public  UserDTO readUser(@NotNull Long id){
        try{
            Optional<UserEntity> userEntity = userRepository.findById(id);
            if (userEntity.isPresent()) {
                return userMapper.toDTO(userEntity.get());
            }
            else{
                throw new IllegalArgumentException("User with id " + id + " does not exist");
            }
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }

    public UserDTO updateUser(@NotNull UserUpdateDTO userUpdateDTO){
        try {
            if (userUpdateDTO.getId() == null) {
                throw new IllegalArgumentException("User not found ");
            }
            Optional<UserEntity> userEntityOptional = userRepository.findById(userUpdateDTO.getId());
            if (userEntityOptional.isEmpty()) {
                throw new IllegalStateException("User with ID " + userUpdateDTO.getId() + " not found");
            }
            UserEntity userEntity = userEntityOptional.get();
            userMapper.toEntity(userUpdateDTO, userEntity);
            userUpdateDTO.getPassword().ifPresent(password -> userEntity.setPassword(passwordEncoder.encode(password)));
            UserEntity updatedUserEntity = userRepository.save(userEntity);
            return userMapper.toDTO(updatedUserEntity);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Could not update user "+ e.getMessage(), e);
        }
    }

    public boolean deleteUser(@NotNull Long id){
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

    public boolean existsUser(@NotNull Long id){
        try {
            return userRepository.existsById(id);
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }

    public boolean existsUser(@NotNull String username){
        try{
            return userRepository.existsByName(username);
        }
        catch (DataAccessException ex){
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }
}
