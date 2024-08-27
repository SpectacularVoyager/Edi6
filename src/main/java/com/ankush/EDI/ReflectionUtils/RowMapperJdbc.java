package com.ankush.EDI.ReflectionUtils;

import com.ankush.EDI.ReflectionUtils.Annotations.SQLSelect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RowMapperJdbc<T> extends SqlJDBCObject<SQLSelect> implements RowMapper<T> {

    Map<String, Field> map;
    Constructor<T> constructor;

    public RowMapperJdbc(Class<T> clazz, JdbcTemplate template) throws NoSuchMethodException {
        super(clazz, template);
        constructor = clazz.getConstructor();
    }

    void init(List<Field> fields) {
        map = new LinkedHashMap<>();
        for (Field f : fields) {
            map.put(PresentOr(f.getAnnotation(SQLSelect.class).col(), f.getName()), f);
        }
        try {
            constructor.newInstance();
        } catch (Exception e) {
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
