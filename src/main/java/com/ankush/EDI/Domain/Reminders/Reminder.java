package com.ankush.EDI.Domain.Reminders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Reminder {
    int id;
    String user;
    String type;
    Date time;
    String message;

    public void insert(JdbcTemplate template) {
        template.update("insert into reminders (username,type,time,message) values (?,?,?,?)", user, type, time, message);
//        template.query();
    }

}
