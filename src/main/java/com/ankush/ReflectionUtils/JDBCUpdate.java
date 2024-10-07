package com.ankush.ReflectionUtils;

import com.ankush.ReflectionUtils.Annotations.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JDBCUpdate extends JDBCGeneric<Update> {
    String table;
    String suffix;
    private String query;
    List<Field> fields = getFields(Update.class);


    public JDBCUpdate(String table, String suffix, Class<?> clazz, JdbcTemplate template) {
        super(clazz, template);
        fields = getFields(Update.class);
        init(fields);
    }

    public JDBCUpdate(String table, String suffix, Class<?> clazz, JdbcTemplate template, String val) {
        super(clazz, template);
        fields = getFields(Update.class, x -> x.col().equals(val));
        init(fields);
    }


    void init(List<Field> fields) {

        List<String> strings = new ArrayList<>();
        for (Field f : fields) {
            strings.add(String.format(" %s = ? ", PresentOr(f.getAnnotation(Update.class).col(), f.getName())));
        }
        query = String.format("UPDATE SET %s %s %s", table, String.join(",", strings), suffix);
    }

    public int update(Object o) throws IllegalAccessException {
        PreparedStatementSetter pss = getArgs(o, fields);
        return template.update(query, pss);
    }
}
