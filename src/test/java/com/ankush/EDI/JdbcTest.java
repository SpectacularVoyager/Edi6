package com.ankush.EDI;

import com.ankush.EDI.ReflectionUtils.InsertJDBCObject;
import com.ankush.EDI.Test.TestDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTest {
    static JdbcTemplate template;
    InsertJDBCObject<TestDAO> insertJDBCObject = new InsertJDBCObject<>("test", TestDAO.class, template);

    @BeforeAll
    static void init() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/edi6");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("Password@123");

        template = new JdbcTemplate(dataSourceBuilder.build());
    }

    @Test
    void run() throws IllegalAccessException {
        System.out.println(template);
//        System.out.println(insertJDBCObject.insert(new TestDAO("HELLO")).getKey().longValue());
    }

}
