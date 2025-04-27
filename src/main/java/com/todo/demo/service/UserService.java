package com.todo.demo.service;

import com.todo.demo.component.SupplierComponent;
import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.model.exception.NotFoundException;
import com.todo.demo.repository.UserRepository;
import com.todo.demo.repository.mapper.UserEntityMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;
    private final SupplierComponent supplierComponent;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(SupplierComponent supplierComponent, UserRepository userRepository, PasswordEncoder passwordEncoder, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.supplierComponent = supplierComponent;
        this.passwordEncoder = passwordEncoder;
        this.userEntityMapper = userEntityMapper;
    }

    public UserDTO createUser(@Valid @NotNull UserCreateDTO userCreateDTO) {
        supplierComponent.validateDto(userCreateDTO);
        Optional<UserDTO> existingUser = userRepository.findByName(userCreateDTO.getName());
        if (existingUser.isPresent()) {
            logger.warn("User with name {} already exists", userCreateDTO.getName());
            throw new IllegalArgumentException("User with name " + userCreateDTO.getName() + " already exists");
        }

        return supplierComponent.handleDatabaseOperation(() -> {
            UserEntity userEntity = userEntityMapper.toEntity(userCreateDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserDTO savedUser = userRepository.save(userEntity);
            logger.info("User created successfully with id: {}", savedUser.getId());
            return savedUser;
        }, "Error creating user");

    }

    public UserDTO readUser(@NotNull String username) {
        return supplierComponent.handleDatabaseOperation(() -> {
            Optional<UserDTO> userDTO = userRepository.findByName(username);
            if (userDTO.isPresent()) {
                logger.debug("User found with username: {}", username);
                return userDTO.get();
            } else {
                logger.warn("User not found with username: {}", username);
                throw new NotFoundException("User not found");
            }
        }, "Error reading user");
    }

    public UserDTO readUser(@NotNull Long id) {
        return supplierComponent.handleDatabaseOperation(() -> {
            Optional<UserDTO> userDTO = userRepository.findById(id);
            if (userDTO.isPresent()) {
                logger.debug("User found with id: {}", id);
                return userDTO.get();
            } else {
                logger.warn("User not found with id: {}", id);
                throw new NotFoundException("User with id " + id + " does not exist");
            }
        }, "Error reading user.");
    }

    public Optional<UserDTO> updateUser(@NotNull UserUpdateDTO userUpdateDTO) {
        supplierComponent.validateDto(userUpdateDTO);
        return supplierComponent.handleDatabaseOperation(() -> {
            Optional<UserEntity> userEntityOptional = userRepository.findEntityById(userUpdateDTO.getId());
            if (userEntityOptional.isEmpty()) {
                logger.warn("User not found with id: {}", userUpdateDTO.getId());
                throw new IllegalStateException("User with ID " + userUpdateDTO.getId() + " not found");
            }
            UserEntity userEntity = userEntityOptional.get();
            userEntityMapper.toEntity(userUpdateDTO, userEntity);
            userUpdateDTO.getPassword().ifPresent(password -> userEntity.setPassword(passwordEncoder.encode(password)));
            Optional<UserDTO> updatedUser = userRepository.update(userEntity);
            if (updatedUser.isPresent()) {
                logger.info("User updated successfully with id: {}", userUpdateDTO.getId());
            }
            return updatedUser;
        }, "Error updating user");
    }

    public boolean deleteUser(@NotNull Long id) {
        return supplierComponent.handleDatabaseOperation(() -> {
            boolean deleted = userRepository.delete(id);
            if (deleted) {
                logger.info("User deleted successfully with id: {}", id);
            } else {
                logger.warn("User not found for deletion with id: {}", id);
            }
            return deleted;
        }, "Error deleting user");
    }

    public ArrayList<UserDTO> readAllUsers() {
        return supplierComponent.handleDatabaseOperation(() -> {
            ArrayList<UserDTO> userDTOArrayList = new ArrayList<>(userRepository.findAll());
            if (userDTOArrayList.isEmpty()) {
                logger.info("No users found");
                throw new NotFoundException("No users found");
            }
            logger.debug("Found {} users", userDTOArrayList.size());
            return userDTOArrayList;
        }, "Error reading all users");
    }

    public boolean checkPassword(@NotNull String password, @NotNull Long id) {
        return supplierComponent.handleDatabaseOperation(() -> {
            Optional<UserEntity> userEntity = userRepository.findEntityById(id);
            if (userEntity.isEmpty()) {
                logger.warn("User not found with id: {}", id);
                throw new NotFoundException("User with id " + id + " does not exist");
            }
            boolean matches = passwordEncoder.matches(password, userEntity.get().getPassword());
            logger.debug("Password check for user with id {}: {}", id, matches ? "successful" : "failed");
            return matches;
        }, "Error checking password");
    }

    public UserDTO login(@NotNull String username, @NotNull String password) {
        return supplierComponent.handleDatabaseOperation(() -> {
            Optional<UserEntity> userEntityOptional = userRepository.findEntityByName(username);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                    logger.warn("Wrong password for username: {}", username);
                    throw new IllegalArgumentException("Wrong password");
                }
                UserDTO userDTO = userRepository.findById(userEntity.getId()).orElseThrow(() -> {
                    logger.error("User with id {} not found after login", userEntity.getId());
                    return new IllegalArgumentException("User with id " + userEntity.getId() + " not found");
                });
                logger.info("User logged in successfully with username: {}", username);
                return userDTO;
            } else {
                logger.warn("User not found with username: {}", username);
                throw new IllegalArgumentException("User not found");
            }
        }, "Error login");
    }


}