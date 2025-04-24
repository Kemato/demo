package com.todo.demo.repository.mapper;

import com.todo.demo.model.dto.UserDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDTORowMapper implements RowMapper<UserDTO> {

    @Override
    public UserDTO mapRow(ResultSet resultSet, int i) throws SQLException{
        UserDTO userDTO = new UserDTO();
        userDTO.setId(resultSet.getLong("id"));
        userDTO.setName(resultSet.getString("name"));
        return userDTO;
    }
}
