package com.ankush.EDI.Domain.Surveys;

import com.ankush.EDI.Utils.Utils;
import com.ankush.ReflectionUtils.JDBCInsert;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("java/api/user/survey")
public class SurveyController {
    JdbcTemplate template;
    RowMapper<Survey> surveyRowMapper;


    public SurveyController(@Autowired JdbcTemplate template) {
        this.template = template;
        this.surveyRowMapper = (rs, rowNum) -> {
            Survey s = new Survey();
            s.setSurveyID(rs.getInt("surveyID"));
            s.setAnswers(Arrays.stream(rs.getString("answers").split(",")).map(Integer::parseInt).toList());
            s.setTimestamp(rs.getTimestamp("timestamp"));
            s.setType(rs.getString("type"));
            return s;
        };
    }

    @PostMapping("post")
    public ResponseEntity<?> post(@RequestBody @Valid Survey survey) throws IllegalAccessException {
        User user = Utils.getUser();
        survey.compute(user.getUsername(), true);
        survey.insert(template);
        return ResponseEntity.ok(survey);
    }

    @GetMapping("get")
    public ResponseEntity<?> get(@RequestParam int id) {
        User user = Utils.getUser();
        Survey s = template.queryForObject("select * from surveys where surveyID=? and userid=?", surveyRowMapper, id, user.getUsername());
        s.compute(user.getUsername(), false);
        return ResponseEntity.ok(s);
    }

    @GetMapping("getRecent")
    public ResponseEntity<?> getAll() {
        User user = Utils.getUser();
        List<Survey> s = template.query("select * from surveys where userid=? order by timestamp desc limit 3", surveyRowMapper, user.getUsername());
        s.forEach(x -> x.compute(user.getUsername(), false));
        return ResponseEntity.ok(s);
    }

}
