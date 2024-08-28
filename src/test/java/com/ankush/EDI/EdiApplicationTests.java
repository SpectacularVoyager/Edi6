package com.ankush.EDI;

import com.ankush.EDI.ReflectionUtils.JDBCInsert;
import com.ankush.EDI.Test.TestDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class EdiApplicationTests {
    @Autowired
    JdbcTemplate template;
    JDBCInsert<TestDAO> JDBCInsert = new JDBCInsert("test", TestDAO.class, template);


    @Test
    void contextLoads() {
    }

    @Test
    void run() throws IllegalAccessException {
        JDBCInsert.insert(new TestDAO("HELLO"));
    }

}
