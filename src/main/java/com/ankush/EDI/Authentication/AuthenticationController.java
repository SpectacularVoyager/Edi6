package com.ankush.EDI.Authentication;

import com.ankush.EDI.Users.AuthUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("java/api/auth")
public class AuthenticationController {
    @Autowired
    JdbcTemplate template;
    @Autowired
    UserDetailsManager users;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthUser user) {
        UserDetails details = User.withUsername(user.getUsername()).password(encoder.encode(user.getPassword())).authorities("USER").build();
        if (users.userExists(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username", "desc", "USER ALREADY EXISTS"));
        }
        users.createUser(details);
        user.insertDetails(template);
        return ResponseEntity.ok("CREATED USER");
    }


}
