package com.example.authdemospring.utils;

import com.example.authdemospring.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setDisplayName(rs.getString("display_name"));
        user.setUsername(rs.getString("username"));
        user.setToken(rs.getString("token"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRoles(rs.getString("roles"));

        return user;
    }
}
