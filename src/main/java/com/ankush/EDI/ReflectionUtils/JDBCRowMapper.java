package com.ankush.EDI.ReflectionUtils;

import com.ankush.EDI.ReflectionUtils.Annotations.Select;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCRowMapper<T> extends JDBCGeneric<Select> implements RowMapper<T> {

    Map<String, Field> map;
    Constructor<T> constructor;

    public JDBCRowMapper(Class<T> clazz, JdbcTemplate template, boolean includeUnAnnotated) {
        super(clazz, template);
        if (includeUnAnnotated)
            init(clazz, getFields().toList());
        else
            init(clazz, getFields(Select.class));
    }

    public JDBCRowMapper(Class<T> clazz, JdbcTemplate template, String val) {
        super(clazz, template);
        init(clazz, getFields(Select.class, x -> x.col().equals(val)));
    }


    public JDBCRowMapper(Class<T> clazz, JdbcTemplate template) {
        this(clazz, template, true);
    }


    void init(Class<T> clazz, List<Field> fields) {

        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {

            throw new RuntimeException(String.format("NO DEFAULT CONSTRUCTOR FOR %s FOUND", clazz.getName()), e);
        }

        map = new LinkedHashMap<>();
        for (Field f : fields) {
            String name = f.getName();
            Select s = f.getAnnotation(Select.class);
            if (s != null) {
                name = PresentOr(s.col(), f.getName());
            }
            map.put(name, f);
        }

        try {
            constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
//        new RowCountCallbackHandler<T>();
        T val;
        try {
            val = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        for (Map.Entry<String, Field> entry : map.entrySet()) {
            try {
                entry.getValue().set(val, rs.getObject(entry.getKey()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return val;
    }
}
