package com.ankush.EDI.Domain.Surveys;

import com.ankush.EDI.Utils.In.In;
import com.ankush.ReflectionUtils.Annotations.AutoGenerated;
import com.ankush.ReflectionUtils.Annotations.Insert;
import com.ankush.ReflectionUtils.Annotations.Select;
import com.ankush.ReflectionUtils.JDBCInsert;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    @Select
    @AutoGenerated
    int id;
    @Select
    @Insert
    String userid;
    List<SurveyAnswer> answers;


    public Survey insert(JDBCInsert<Survey> insert, JDBCInsert<SurveyAnswer> answer) throws IllegalAccessException {
        int id = insert.insert(this).getKey().intValue();
        this.id = id;
        for (SurveyAnswer a : answers) {
            a.setSurveyId(id);
            answer.insert(a);
        }
        return this;
    }

    public Survey get(JdbcTemplate template, JDBCRowMapper<Survey> mapper, JDBCRowMapper<SurveyAnswer> answerMapper, int id) {
        Survey s = template.queryForObject("select * from surveys where id=?", mapper, id);
        answers = template.query("select * from survey_answers where surveyID=?", answerMapper, s.getId());
        return s;
    }

    public void addAnswer(SurveyAnswer a) {
        a.surveyId = id;
        this.answers.add(a);
    }
}
