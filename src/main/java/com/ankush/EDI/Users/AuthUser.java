package com.ankush.EDI.Users;

import com.ankush.EDI.Domain.Reminders.Reminder;
import com.ankush.ReflectionUtils.Annotations.Select;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Component
@ToString
public class AuthUser {
    @Select
    @NotBlank
    private String username;
    @Select
    @Email
    private String email;
    @Select
    @NotBlank
    private String name;
    @Select
    @NotNull
    private Date birthdate;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Select
    private String gender;
    @Select
    private String bloodgroup;
    @Select
    private String data;
    @Select
    private boolean hasConsented;
    @Select
    private String previousChallenges;
    @Select
    private String currentChallenges;


    List<Reminder> reminders = new ArrayList<>();

    public void insertDetails(JdbcTemplate template) {
        template.update("insert into UserDetails (username,name,email,birthdate,gender,bloodgroup,data,hasConsented,previousChallenges,currentChallenges) values (?,?,?,?,?,?,?)", username, name, email, birthdate, gender, bloodgroup, data, hasConsented, previousChallenges, currentChallenges);
        reminders.forEach(x -> x.insert(template));
    }

    public void update(JdbcTemplate template) {
        template.update("update UserDetails  " +
                "set name=?,email=?,birthdate=?,gender=?,bloodgroup=?,data=?,hasConsented=?,previousChallenges=?,currentChallenges=? where username=?", name, email, birthdate, gender, bloodgroup, data, username, hasConsented, previousChallenges, currentChallenges);
    }

    public static JDBCRowMapper<AuthUser> get(JdbcTemplate template) {
        return new JDBCRowMapper<>(AuthUser.class, template, false);
    }
}
