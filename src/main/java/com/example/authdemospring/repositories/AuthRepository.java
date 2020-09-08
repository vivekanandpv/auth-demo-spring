package com.example.authdemospring.repositories;

import com.example.authdemospring.models.User;
import com.example.authdemospring.utils.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUser(String username) {
        String sql = "SELECT id, username, display_name, password_hash, token, roles FROM users WHERE username =?";
        RowMapper<User> rowMapper = new UserRowMapper();
        return this.jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    public boolean getLoginStatus(String username, String token) {
        String sql = "SELECT COUNT(id) FROM users WHERE username =? AND token =?";
        int result = this.jdbcTemplate.queryForObject(sql, new Object[] {username, token}, Integer.class);

        return result > 0;
    }

    public void register(User user) {
        String sql = "INSERT INTO public.users(username, display_name, password_hash, roles)	VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.update(sql, user.getUsername(), user.getDisplayName(),
                user.getPasswordHash(), user.getRoles());
    }

    public void logout(String username) {
        String sql = "UPDATE public.users SET token = '' WHERE username=?";
        this.jdbcTemplate.update(sql, username);
    }

    public User login(String username, String token) {
        String sql = "UPDATE public.users SET token = ? WHERE username=?";
        this.jdbcTemplate.update(sql, token, username);
        return this.getUser(username);
    }
}
