package com.ankush.EDI.Domain.ActivityLog;

import com.ankush.ReflectionUtils.JDBCInsert;
import com.ankush.ReflectionUtils.JDBCRowMapper;
import com.ankush.EDI.Utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("java/api/user/activity")
public class ActivityController {
    @Autowired
    JdbcTemplate template;
    JDBCRowMapper<ActivityLog> activityMapper;
    JDBCInsert<ActivityLog> activityInsert;

    public ActivityController(@Autowired JdbcTemplate template) {
        this.template = template;
        System.out.println(template);
        activityMapper = new JDBCRowMapper<>(ActivityLog.class, template);
        activityInsert = new JDBCInsert<>("activity_log", ActivityLog.class, template);

    }

    @GetMapping("getAll")
    public List<ActivityLog> getActivity() {
        String username = Utils.getUser().getUsername();
        return template.query("select * from activity_log where username=?", activityMapper, username);
    }

    @PostMapping("add")
    public ResponseEntity<?> addActivity(@RequestBody @Valid ActivityLog activity) throws IllegalAccessException {
        String username = Utils.getUser().getUsername();
        activity.setUsername(username);
        KeyHolder kh = activityInsert.insert(activity);
        if (kh.getKey() != null)
            activity.setId(kh.getKey().longValue());
        return ResponseEntity.ok(activity);
    }

}
