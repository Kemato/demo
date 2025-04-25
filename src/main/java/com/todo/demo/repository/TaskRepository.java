package com.todo.demo.repository;

import com.todo.demo.model.dto.TaskDTO;
import com.todo.demo.model.entity.TaskEntity;
import com.todo.demo.repository.mapper.TaskDTORowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
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
public class TaskRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<TaskDTO> taskDTORowMapper;
    private final String sqlQueryToTaskDTO = """
                SELECT
                    t.id,
                    t.title,
                    t.status,
                    u1.name AS author,
                    t.priority,
                    t.deadline,
                    u2.name AS assignee,
                    t.description,
                    t.date_created,
                    t.date_updated,
                    t.date_finished
                FROM tasks t
                JOIN users u1 ON t.author = u1.id
                LEFT JOIN users u2 ON t.assignee = u2.id
                """;

    public TaskRepository(DataSource dataSource, TaskDTORowMapper taskDTORowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.taskDTORowMapper = taskDTORowMapper;
    }

    @Transactional
    public TaskDTO save(TaskEntity taskEntity) {
        String sql = "INSERT INTO tasks (title, description, status, priority, assignee, author, deadline) " +
                "VALUES (:title, :description, :status, :priority, :assignee, :author, :deadline) RETURNING id";
        SqlParameterSource params = new BeanPropertySqlParameterSource(taskEntity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Transactional
    public Optional<TaskDTO> findById(Long id) {
        String sql = sqlQueryToTaskDTO + "WHERE t.id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        List<TaskDTO> tasks = jdbcTemplate.query(sql, params, taskDTORowMapper);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.getFirst());
    }

    @Transactional
    public Optional<TaskEntity> findEntityById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(TaskEntity.class)));
    }

    @Transactional
    public Optional<TaskDTO> findByTitle(String title) {
        String sql = sqlQueryToTaskDTO + "WHERE t.title = :title";
        SqlParameterSource params = new MapSqlParameterSource("title", title);
        List<TaskDTO> tasks = jdbcTemplate.query(sql, params, taskDTORowMapper);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.getFirst());
    }

    @Transactional
    public List<TaskDTO> findAll() {
        return jdbcTemplate.query(sqlQueryToTaskDTO, taskDTORowMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.update(sql, params) > 0;
    }

    @Transactional
    public Optional<TaskDTO> update(TaskEntity taskEntity) {
        String sql = """
                UPDATE tasks SET
                title = :title,
                description = :description,
                status = :status,
                priority = :priority,
                assignee = :assignee,
                deadline = :deadline,
                date_updated = :dateUpdated,
                date_finished = :dateFinished
                WHERE id = :id
                """;
        SqlParameterSource params = new BeanPropertySqlParameterSource(taskEntity);
        jdbcTemplate.update(sql, params);
        return findById(taskEntity.getId());
    }

    @Transactional
    public List<TaskDTO> findAllByAssignee(Long assignee) {
        String sql = sqlQueryToTaskDTO + "WHERE assignee = :assignee";
        SqlParameterSource params = new MapSqlParameterSource("assignee", assignee);
        return jdbcTemplate.query(sql, params, taskDTORowMapper);
    }

    @Transactional
    public List<TaskDTO> findAllByAuthor(Long author) {
        String sql = sqlQueryToTaskDTO + "WHERE author = :author";
        SqlParameterSource params = new MapSqlParameterSource("author", author);
        return jdbcTemplate.query(sql, params, taskDTORowMapper);
    }
}