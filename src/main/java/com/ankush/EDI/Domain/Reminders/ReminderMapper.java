package com.ankush.EDI.Domain.Reminders;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReminderMapper implements RowMapper<Reminder> {
    @Override
    public Reminder mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reminder reminder = new Reminder();
        reminder.setId(rs.getInt("r.id"));
        reminder.setUser(rs.getString("r.user"));
        reminder.setTime(rs.getDate("r.time"));
        reminder.setMessage(rs.getString("r.message"));
        reminder.setType(rs.getString("r.type"));
        return reminder;
    }
}
