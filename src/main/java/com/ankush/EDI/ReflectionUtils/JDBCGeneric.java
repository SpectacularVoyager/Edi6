package com.ankush.EDI.ReflectionUtils;

import com.ankush.EDI.ReflectionUtils.Annotations.Ignore;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@AllArgsConstructor
public class JDBCGeneric<A extends Annotation> {
    Class<?> clazz;
    JdbcTemplate template;

    protected List<Field> getFields(Class<? extends Annotation> annotation) {
        return getFields().filter(x -> x.isAnnotationPresent(annotation)).toList();
    }

    protected List<Field> getFields(Class<? extends Annotation> annotation, Predicate<A> pred) {
        return getFields().filter(x -> x.isAnnotationPresent(annotation)).filter(x -> pred.test((A) x.getAnnotation(annotation))).toList();
    }

//    protected Stream<Field> getFields() {
//        return Arrays.stream(clazz.getDeclaredFields());
//    }

    protected Stream<Field> getFields() {
        return Arrays.stream(clazz.getDeclaredFields())
//                .filter(x -> Modifier.isPrivate(x.getModifiers()))
                .filter(x-> !x.isAnnotationPresent(Ignore.class))
                .map(x -> {
                    x.setAccessible(true);
                    return x;
                });
    }


    protected String PresentOr(String a, String b) {
        if (a == null) return b;
        return a.isEmpty() ? b : a;
    }

    public PreparedStatementSetter getArgs(Object o, List<Field> fields) throws IllegalAccessException {
        Object[] args = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            args[i] = fields.get(i).get(o);
        }
        return new ArgumentPreparedStatementSetter(args);
    }

    protected List<Field> makeAccessible(List<Field> fields) {
        List<Field> out = new ArrayList<>();
        for (Field f : fields) {
            if (Modifier.isPrivate(f.getModifiers())) {
                continue;
            }
            if (Modifier.isProtected(f.getModifiers())) {
//                privateFields.add(field);
                f.setAccessible(true);
            }
            out.add(f);
        }
        return out;
    }

}
