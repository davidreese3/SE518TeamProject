/*package com.se518.teamproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class BruteForceProtectionService {

    private static final int MAX_ATTEMPT = 5;
    private static final long LOCK_TIME_MINUTES = 15;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BruteForceProtectionService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void loginSucceeded(String username) {
        String query = "DELETE FROM login_attempts WHERE username = ?";
        jdbcTemplate.update(query, username);
    }

    public void loginFailed(String username) {
        String selectQuery = "SELECT attempts FROM login_attempts WHERE username = ?";
        Integer attempts = jdbcTemplate.queryForObject(selectQuery, new Object[]{username}, Integer.class);

        if (attempts == null) {
            String insertQuery = "INSERT INTO login_attempts (username, attempts, lock_time) VALUES (?, ?, NULL)";
            jdbcTemplate.update(insertQuery, username, 1);
        } else {
            attempts++;
            if (attempts >= MAX_ATTEMPT) {
                String updateQuery = "UPDATE login_attempts SET attempts = ?, lock_time = ? WHERE username = ?";
                jdbcTemplate.update(updateQuery, attempts, Timestamp.valueOf(LocalDateTime.now()), username);
            } else {
                String updateQuery = "UPDATE login_attempts SET attempts = ? WHERE username = ?";
                jdbcTemplate.update(updateQuery, attempts, username);
            }
        }
    }

    public boolean isBlocked(String username) {
        String selectQuery = "SELECT lock_time FROM login_attempts WHERE username = ?";
        Timestamp lockTime = jdbcTemplate.queryForObject(selectQuery, new Object[]{username}, Timestamp.class);

        if (lockTime == null) {
            return false;
        }

        LocalDateTime lockExpiry = lockTime.toLocalDateTime().plusMinutes(LOCK_TIME_MINUTES);
        if (LocalDateTime.now().isAfter(lockExpiry)) {
            String updateQuery = "UPDATE login_attempts SET lock_time = NULL, attempts = 0 WHERE username = ?";
            jdbcTemplate.update(updateQuery, username);
            return false;
        }

        return true;
    }

}*/
