package com.ankush.EDI.Test;

import com.ankush.EDI.ReflectionUtils.Annotations.AutoGenerated;
import com.ankush.EDI.ReflectionUtils.Annotations.Insert;

public class TestDAO {
    @AutoGenerated
    public int id;
    @Insert(stmt = "")
    public String name;


    public TestDAO(String name) {
        this.name = name;
    }
}
