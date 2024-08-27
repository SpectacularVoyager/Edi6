package com.ankush.EDI.Users;

import com.ankush.EDI.Domain.Reminders.Reminder;
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
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;


    @NotBlank
    private String name;

    @NotNull
    private Date birthdate;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    List<Reminder> reminders = new ArrayList<>();

    public void insertDetails(JdbcTemplate template) {
        template.update("insert into UserDetails (username,name,email,birthdate) values (?,?,?,?)", username, name, email, birthdate);
        reminders.forEach(x -> x.insert(template));
    }
}
