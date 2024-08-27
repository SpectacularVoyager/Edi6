package com.ankush.EDI.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;

public class Utils {
    public static String generateInsertQuery(String table, String... columns) {
        String q = "?".repeat(columns.length);
        String cols = Arrays.stream(columns).reduce("", String::concat);
        return String.format("insert into %s (%s) values (%s)", table, cols, q);
    }

    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
