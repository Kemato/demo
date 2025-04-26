package com.todo.demo.repository;

import com.todo.demo.model.entity.UserEntity;
import com.todo.demo.repository.mapper.UserDTORowMapper;
import com.todo.demo.model.dto.UserDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {
    private final UserDTORowMapper userDTORowMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource, UserDTORowMapper userDTORowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userDTORowMapper = userDTORowMapper;
    }

    @Transactional
    public UserDTO save(UserEntity userEntity) {
        String sql = "INSERT INTO users (name, password) VALUES (:name, :password) RETURNING id";
        SqlParameterSource params = new BeanPropertySqlParameterSource(userEntity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to save"));
    }

    @Transactional
    public Optional<UserDTO> findById(Long id) {
        String sql = "SELECT id, name FROM users WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        List<UserDTO> users = jdbcTemplate.query(sql, params, userDTORowMapper);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }

    @Transactional
    public Optional<UserDTO> findByName(String name) {
        String sql = "SELECT id, name FROM users WHERE name = :name";
        SqlParameterSource params = new MapSqlParameterSource("name", name);
        List<UserDTO> users = jdbcTemplate.query(sql, params, userDTORowMapper);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }

    @Transactional
    public Optional<UserEntity> findEntityById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        UserEntity userEntity = jdbcTemplate.queryForObject(
                sql,
                params,
                new BeanPropertyRowMapper<>(UserEntity.class));
        return Optional.ofNullable(userEntity);
    }

    @Transactional
    public Optional<UserEntity> findEntityByName(String name) {
        String sql = "SELECT * FROM users WHERE name = :name";
        SqlParameterSource params = new MapSqlParameterSource("name", name);

        try {
            UserEntity userEntity = jdbcTemplate.queryForObject(
                    sql,
                    params,
                    new BeanPropertyRowMapper<>(UserEntity.class)
            );
            return Optional.ofNullable(userEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<UserDTO> findAll() {
        String sql = "SELECT id, name FROM users";
        return jdbcTemplate.query(sql, userDTORowMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        String sql = "delete from users where id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.update(sql, params) > 0;
    }

    @Transactional
    public Optional<UserDTO> update(UserEntity userEntity) {
        String sql = "UPDATE users SET name = :name, password = :password WHERE id = :id";
        SqlParameterSource params = new BeanPropertySqlParameterSource(userEntity);
        jdbcTemplate.update(sql, params);
        return findById(userEntity.getId());
    }
}
