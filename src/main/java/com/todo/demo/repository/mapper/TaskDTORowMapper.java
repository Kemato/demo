package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.TaskDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TaskDTORowMapper implements RowMapper<TaskDTO> {

    @Override
    public TaskDTO mapRow(ResultSet resultSet, int i) throws SQLException{
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        String author = resultSet.getString("author");
        String status = resultSet.getString("status");
        String assignee = resultSet.getString("assignee");
        String priority = resultSet.getString("priority");

        java.sql.Timestamp dateCreatedTimestamp = resultSet.getTimestamp("date_created");
        LocalDateTime dateCreated = dateCreatedTimestamp != null ? dateCreatedTimestamp.toLocalDateTime() : null;

        java.sql.Timestamp dateUpdatedTimestamp = resultSet.getTimestamp("date_updated");
        LocalDateTime dateUpdated = dateUpdatedTimestamp != null ? dateUpdatedTimestamp.toLocalDateTime() : null;

        java.sql.Timestamp dateFinishedTimestamp = resultSet.getTimestamp("date_finished");
        LocalDateTime dateFinished = dateFinishedTimestamp != null ? dateFinishedTimestamp.toLocalDateTime() : null;

        java.sql.Timestamp deadlineTimestamp = resultSet.getTimestamp("deadline");
        LocalDateTime deadline = deadlineTimestamp != null ? deadlineTimestamp.toLocalDateTime() : null;
        return new TaskDTO(
                id,
                title,
                description,
                status,
                priority,
                assignee,
                author,
                dateCreated,
                dateUpdated,
                deadline,
                dateFinished
        );
    }
}
