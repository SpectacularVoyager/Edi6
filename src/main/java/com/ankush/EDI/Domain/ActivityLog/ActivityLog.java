package com.ankush.EDI.Domain.ActivityLog;

import com.ankush.ReflectionUtils.Annotations.AutoGenerated;
import com.ankush.ReflectionUtils.Annotations.Insert;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class ActivityLog {
    @AutoGenerated
    long id;
    @Insert
    String username;
    @Insert
    String activityType;
    @Insert
    String activityDetails;
    @Insert
    Timestamp timestamp;

    public ActivityLog() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
