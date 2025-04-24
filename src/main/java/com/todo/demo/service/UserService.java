package com.todo.demo.service;


import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.repository.UserRepository;
import com.todo.demo.repository.mapper.UserEntityMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEntityMapper = userEntityMapper;
    }

    public UserDTO createUser(@Valid @NotNull UserCreateDTO userCreateDTO) {
        try {
            if (userCreateDTO.getName() == null || userCreateDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            Optional<UserDTO> existingUser = userRepository.findByName(userCreateDTO.getName());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("User with name " + userCreateDTO.getName() + " already exists");
            }
            UserEntity userEntity = userEntityMapper.toEntity(userCreateDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            return userRepository.save(userEntity);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error creating user" + e.getMessage(), e);
        }
    }

    public UserDTO readUser(@NotNull String username) {
        try {
            Optional<UserDTO> userDTO = userRepository.findByName(username);
            if (userDTO.isPresent()) {
                return userDTO.get();
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error reading user " + e.getMessage(), e);
        }
    }

    public UserDTO readUser(@NotNull Long id) {
        try {
            Optional<UserDTO> userDTO = userRepository.findById(id);
            if (userDTO.isPresent()) {
                return userDTO.get();
            } else {
                throw new IllegalArgumentException("User with id " + id + " does not exist");
            }
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error while finding user " + ex.getMessage(), ex);
        }
    }

    public Optional<UserDTO> updateUser(@NotNull UserUpdateDTO userUpdateDTO) {
        try {
            if (userUpdateDTO.getId() == null) {
                throw new IllegalArgumentException("User not found ");
            }
            Optional<UserEntity> userEntityOptional = userRepository.findEntityById(userUpdateDTO.getId());
            if (userEntityOptional.isEmpty()) {
                throw new IllegalStateException("User with ID " + userUpdateDTO.getId() + " not found");
            }
            UserEntity userEntity = userEntityOptional.get();
            userEntityMapper.toEntity(userUpdateDTO, userEntity);
            userUpdateDTO.getPassword().ifPresent(password -> userEntity.setPassword(passwordEncoder.encode(password)));
            return userRepository.update(userEntity);
        } catch (DataAccessException e) {
            throw new RuntimeException("Could not update user " + e.getMessage(), e);
        }
    }

    public boolean deleteUser(@NotNull Long id) {
        try {
            return userRepository.delete(id);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error while deleting user " + ex.getMessage(), ex);
        }
    }

    public ArrayList<UserDTO> readAllUsers() {
        try {
            ArrayList<UserDTO> userDTOArrayList = new ArrayList<>(userRepository.findAll());
            if (userDTOArrayList.isEmpty()) {
                throw new IllegalArgumentException("No users found");
            }
            return userDTOArrayList;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error while finding users " + ex.getMessage(), ex);
        }
    }

    public boolean checkPassword(@NotNull String password, @NotNull Long id) {
        try {
            Optional<UserEntity> userEntity = userRepository.findEntityById(id);
            if (userEntity.isEmpty()) throw new IllegalArgumentException("User with id " + id + " does not exist");
            return passwordEncoder.matches(password, userEntity.get().getPassword());
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error while checking password " + ex.getMessage(), ex);
        }
    }

    public UserDTO login(@NotNull String username, @NotNull String password) {
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findEntityByName(username);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                    throw new IllegalArgumentException("Wrong password");
                } else {
                    return userRepository.findById(userEntity.getId()).orElseThrow(() -> new IllegalArgumentException("User with id " + userEntity.getId() + " not found"));
                }
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error while checking password " + ex.getMessage(), ex);
        }
    }
}
