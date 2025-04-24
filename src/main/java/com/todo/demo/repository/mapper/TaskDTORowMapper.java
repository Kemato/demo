package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.TaskDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        LocalDateTime dateCreated = resultSet.getTimestamp("date_created").toLocalDateTime();
        LocalDateTime dateUpdated = resultSet.getTimestamp("date_updated").toLocalDateTime();
        LocalDateTime dateFinished = resultSet.getTimestamp("date_finished").toLocalDateTime();
        LocalDateTime deadLine = resultSet.getTimestamp("deadline").toLocalDateTime();
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
                deadLine,
                dateFinished
        );
    }
}
