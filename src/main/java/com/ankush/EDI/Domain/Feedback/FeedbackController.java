package com.ankush.EDI.Domain.Feedback;

import com.ankush.EDI.ReflectionUtils.JDBCInsert;
import com.ankush.EDI.ReflectionUtils.JDBCRowMapper;
import com.ankush.EDI.Utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("java/api/user/feedback")
public class FeedbackController {
    JdbcTemplate template;
    JDBCRowMapper<Feedback> feedbackMapper;
    JDBCInsert<Feedback> feedbackInsert;

    public FeedbackController(@Autowired JdbcTemplate template) {
        this.template = template;
        feedbackMapper = new JDBCRowMapper<>(Feedback.class, template);
        feedbackInsert = new JDBCInsert<>("feedback", Feedback.class, template);
    }

    @GetMapping("getAll")
    public List<Feedback> getFeedbacks() {
        String username = Utils.getUser().getUsername();
        return template.query("select * from feedback where username=?", feedbackMapper, username);
    }

    @PostMapping("add")
    public ResponseEntity<?> addFeedback(@RequestBody @Valid Feedback feedback) throws IllegalAccessException {
        String username = Utils.getUser().getUsername();
        feedback.setUsername(username);
        KeyHolder kh = feedbackInsert.insert(feedback);
        if (kh.getKey() != null)
            feedback.setId(kh.getKey().longValue());
        return ResponseEntity.ok(feedback);
    }
}
