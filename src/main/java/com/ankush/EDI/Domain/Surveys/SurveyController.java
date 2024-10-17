package com.ankush.EDI.Domain.Surveys;

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
@RequestMapping("java/api/user/survey")
public class SurveyController {
    JdbcTemplate template;

    JDBCInsert<Survey> insert;
    JDBCInsert<SurveyAnswer> answerInsert;

    JDBCRowMapper<Survey> mapper;
    JDBCRowMapper<SurveyAnswer> answerMapper;


    public SurveyController(@Autowired JdbcTemplate template) {
        this.template = template;
        insert = new JDBCInsert<>("surveys", Survey.class, template);
        answerInsert = new JDBCInsert<>("survey_answers", SurveyAnswer.class, template);
        mapper=new JDBCRowMapper<>(Survey.class,template);
        answerMapper=new JDBCRowMapper<>(SurveyAnswer.class,template);
    }

    @PostMapping("post")
    public ResponseEntity<?> post(@RequestBody @Valid Survey survey) throws IllegalAccessException {
        User user = Utils.getUser();
        survey.setUserid(user.getUsername());
        return ResponseEntity.ok(survey.insert(insert, answerInsert));
    }

    @GetMapping("get")
    public ResponseEntity<?> get(@RequestParam int id) {
        User user = Utils.getUser();
        Survey s = template.queryForObject("select * from surveys where id=? and userid=?", mapper, id, user.getUsername());
        s.setUserid(user.getUsername());
        s.setAnswers(template.query("select * from survey_answers where surveyID=?", answerMapper, s.getId()));
        return ResponseEntity.ok(s);

    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAll() {
        User user = Utils.getUser();
        List<Survey> s = template.query("select * from surveys where userid=?", mapper, user.getUsername());
        return ResponseEntity.ok(s);
    }

}
