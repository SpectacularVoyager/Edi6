package com.ankush.EDI.User;

import com.ankush.EDI.Users.AuthUser;
import com.ankush.EDI.Utils.Utils;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("java/api/user")
public class UserSpaceController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("profile")
    public ResponseEntity<?> user(@RequestBody AuthUser user) {
        user.update(template);
        return ResponseEntity.ok(user);
    }

    @GetMapping("get")
    public ResponseEntity<?> get() {
        User u = Utils.getUser();

        JDBCRowMapper<AuthUser> mapper = AuthUser.get(template);
        AuthUser user = template.queryForObject("select * from UserDetails where username = ?", mapper, u.getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping("test")
    public String test() {
        return "TEST";
    }
}
