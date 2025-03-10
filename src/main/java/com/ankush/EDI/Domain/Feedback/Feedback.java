package com.ankush.EDI.Domain.Feedback;

import com.ankush.ReflectionUtils.Annotations.AutoGenerated;
import com.ankush.ReflectionUtils.Annotations.Insert;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Feedback {
     @AutoGenerated
    long id;
    @Insert
    String username;
    @Insert
    String content;
    @Insert
    int rating;
    @Insert
    Timestamp timestamp;

    public Feedback() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }
}
