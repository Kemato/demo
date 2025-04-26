package com.todo.demo.service;

import com.todo.demo.model.dto.UserCreateDTO;
import com.todo.demo.model.dto.UserDTO;
import com.todo.demo.model.dto.UserUpdateDTO;
import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.model.exception.NotFoundException;
import com.todo.demo.repository.UserRepository;
import com.todo.demo.repository.mapper.UserEntityMapper;
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

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEntityMapper = userEntityMapper;
    }

    public UserDTO createUser(@Valid @NotNull UserCreateDTO userCreateDTO) {
        try {
            if (userCreateDTO.getName() == null || userCreateDTO.getName().trim().isEmpty()) {
                logger.warn("Username cannot be null or empty: {}", userCreateDTO);
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().trim().isEmpty()) {
                logger.warn("Password cannot be null or empty: {}", userCreateDTO);
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            Optional<UserDTO> existingUser = userRepository.findByName(userCreateDTO.getName());
            if (existingUser.isPresent()) {
                logger.warn("User with name {} already exists", userCreateDTO.getName());
                throw new IllegalArgumentException("User with name " + userCreateDTO.getName() + " already exists");
            }
            UserEntity userEntity = userEntityMapper.toEntity(userCreateDTO);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            UserDTO savedUser = userRepository.save(userEntity);
            logger.info("User created successfully with id: {}", savedUser.getId());
            return savedUser;
        } catch (DataAccessException e) {
            logger.error("Error creating user: {}", userCreateDTO, e);
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error creating user: {}", userCreateDTO, e);
            throw new RuntimeException("Unexpected error creating user: " + e.getMessage(), e);
        }
    }

    public UserDTO readUser(@NotNull String username) {
        try {
            Optional<UserDTO> userDTO = userRepository.findByName(username);
            if (userDTO.isPresent()) {
                logger.debug("User found with username: {}", username);
                return userDTO.get();
            } else {
                logger.warn("User not found with username: {}", username);
                throw new NotFoundException("User not found");
            }
        } catch (DataAccessException e) {
            logger.error("Error reading user with username {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error reading user: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error reading user with username {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Unexpected error reading user: " + e.getMessage(), e);
        }
    }

    public UserDTO readUser(@NotNull Long id) {
        try {
            Optional<UserDTO> userDTO = userRepository.findById(id);
            if (userDTO.isPresent()) {
                logger.debug("User found with id: {}", id);
                return userDTO.get();
            } else {
                logger.warn("User not found with id: {}", id);
                throw new NotFoundException("User with id " + id + " does not exist");
            }
        } catch (DataAccessException ex) {
            logger.error("Error reading user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Error while finding user: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading user: " + ex.getMessage(), ex);
        }
    }

    public Optional<UserDTO> updateUser(@NotNull UserUpdateDTO userUpdateDTO) {
        try {
            if (userUpdateDTO.getId() == null) {
                logger.warn("User id cannot be null for update: {}", userUpdateDTO);
                throw new IllegalArgumentException("User not found");
            }
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
        } catch (DataAccessException e) {
            logger.error("Error updating user with id {}: {}", userUpdateDTO.getId(), e.getMessage(), e);
            throw new RuntimeException("Could not update user: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error updating user with id {}: {}", userUpdateDTO.getId(), e.getMessage(), e);
            throw new RuntimeException("Unexpected error updating user: " + e.getMessage(), e);
        }
    }

    public boolean deleteUser(@NotNull Long id) {
        try {
            boolean deleted = userRepository.delete(id);
            if (deleted) {
                logger.info("User deleted successfully with id: {}", id);
            } else {
                logger.warn("User not found for deletion with id: {}", id);
            }
            return deleted;
        } catch (DataAccessException ex) {
            logger.error("Error deleting user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Error while deleting user: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error deleting user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error deleting user: " + ex.getMessage(), ex);
        }
    }

    public ArrayList<UserDTO> readAllUsers() {
        try {
            ArrayList<UserDTO> userDTOArrayList = new ArrayList<>(userRepository.findAll());
            if (userDTOArrayList.isEmpty()) {
                logger.info("No users found");
                throw new NotFoundException("No users found");
            }
            logger.debug("Found {} users", userDTOArrayList.size());
            return userDTOArrayList;
        } catch (DataAccessException ex) {
            logger.error("Error reading all users: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error while finding users: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error reading all users: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error reading all users: " + ex.getMessage(), ex);
        }
    }

    public boolean checkPassword(@NotNull String password, @NotNull Long id) {
        try {
            Optional<UserEntity> userEntity = userRepository.findEntityById(id);
            if (userEntity.isEmpty()) {
                logger.warn("User not found with id: {}", id);
                throw new NotFoundException("User with id " + id + " does not exist");
            }
            boolean matches = passwordEncoder.matches(password, userEntity.get().getPassword());
            logger.debug("Password check for user with id {}: {}", id, matches ? "successful" : "failed");
            return matches;
        } catch (DataAccessException ex) {
            logger.error("Error checking password for user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Error while checking password: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error checking password for user with id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error checking password: " + ex.getMessage(), ex);
        }
    }

    public UserDTO login(@NotNull String username, @NotNull String password) {
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findEntityByName(username);
            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                if (!passwordEncoder.matches(password, userEntity.getPassword())) {
                    logger.warn("Wrong password for username: {}", username);
                    throw new IllegalArgumentException("Wrong password");
                }
                UserDTO userDTO = userRepository.findById(userEntity.getId())
                        .orElseThrow(() -> {
                            logger.error("User with id {} not found after login", userEntity.getId());
                            return new IllegalArgumentException("User with id " + userEntity.getId() + " not found");
                        });
                logger.info("User logged in successfully with username: {}", username);
                return userDTO;
            } else {
                logger.warn("User not found with username: {}", username);
                throw new IllegalArgumentException("User not found");
            }
        } catch (DataAccessException ex) {
            logger.error("Error during login for username {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Error while checking password: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Unexpected error during login for username {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error during login: " + ex.getMessage(), ex);
        }
    }
}