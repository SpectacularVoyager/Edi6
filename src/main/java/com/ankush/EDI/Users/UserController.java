package com.ankush.EDI.Users;

import com.ankush.EDI.Domain.Reminders.Reminder;
import com.ankush.EDI.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("java/api/user")
public class UserController {
    @Autowired
    JdbcTemplate template;
    static UserMapper userMapper = new UserMapper();

    @GetMapping("user")
    public AuthUser getUser() {
        return getUser(template);
    }
//    @PostMapping("modifyUser")
//    public AuthUser setReminders(@RequestBody AuthUser user){
//
//    }

    public static AuthUser getUser(JdbcTemplate template) {
        User user = Utils.getUser();
        return template.queryForObject("select * from UserDetails where username=?", userMapper, user.getUsername());
    }
}
