package com.ankush.EDI.Domain.Response;

import com.ankush.EDI.Domain.Feedback.Feedback;
import com.ankush.EDI.Utils.Utils;
import com.ankush.ReflectionUtils.JDBCInsert;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("java/api/response")
public class ResponseController {
    JdbcTemplate template;
    JDBCInsert<Response> responseInsert;
    JDBCRowMapper<Response> responseRowMapper;

    public ResponseController(@Autowired JdbcTemplate template) {
        this.template = template;
        responseInsert = new JDBCInsert<>("responses", Response.class, template);
        responseRowMapper = new JDBCRowMapper<>(Response.class, template);
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@Valid @RequestBody Response response) throws IllegalAccessException {
        User user = Utils.getUser();
        response.setUsername(user.getUsername());
        responseInsert.insert(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Response>> getAll() {
        User user = Utils.getUser();
        return ResponseEntity.ok(template.query("select * from responses where username=?", responseRowMapper));
    }
}
