package com.ankush.EDI.Users;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<AuthUser> {
    @Override
    public AuthUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUser user = new AuthUser();
        user.setEmail(rs.getString("email"));
        user.setBirthdate(rs.getDate("birthdate"));
        user.setUsername(rs.getString("username"));
        user.setName(rs.getString("name"));
        return user;
    }
}
