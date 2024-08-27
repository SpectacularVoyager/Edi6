package com.ankush.EDI;

import com.ankush.EDI.ReflectionUtils.InsertJDBCObject;
import com.ankush.EDI.Test.TestDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class EdiApplicationTests {
    @Autowired
    JdbcTemplate template;
    InsertJDBCObject<TestDAO> insertJDBCObject = new InsertJDBCObject("test", TestDAO.class, template);


    @Test
    void contextLoads() {
    }

    @Test
    void run() throws IllegalAccessException {
        insertJDBCObject.insert(new TestDAO("HELLO"));
    }

}
