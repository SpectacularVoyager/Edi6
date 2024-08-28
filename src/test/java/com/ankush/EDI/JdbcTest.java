package com.ankush.EDI;

import com.ankush.EDI.ReflectionUtils.JDBCInsert;
import com.ankush.EDI.ReflectionUtils.JDBCRowMapper;
import com.ankush.EDI.Test.TestDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTest {
    static JdbcTemplate template;
    JDBCInsert<TestDAO> JDBCInsert = new JDBCInsert<>("test", TestDAO.class, template);
    JDBCRowMapper<TestDAO> JDBCRowMapper = new JDBCRowMapper<>(TestDAO.class, template);

    public JdbcTest() throws NoSuchMethodException {
    }

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
//        System.out.println(template.query("select * from test", JDBCRowMapper));
//        System.out.println(insertJDBCObject.insert(new TestDAO("HELLO")).getKey().longValue());
    }

}
