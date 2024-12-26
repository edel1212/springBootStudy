package com.yoo.p6spy.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.util.StringUtils;

public class CustomP6spySqlFormat implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (!StringUtils.hasText(sql))  return String.format("%s %dms", category, elapsed);
        return String.format("%s %dms %s", category, elapsed, highlight(format(sql)));
    }

    private String format(String sql) {
        if (isDDL(sql)) {
            return FormatStyle.DDL.getFormatter().format(sql);
        } else if (isBasic(sql)) {
            return FormatStyle.BASIC.getFormatter().format(sql);
        }
        return sql;
    }

    private String highlight(String sql) {
        return FormatStyle.HIGHLIGHT.getFormatter().format(sql);
    }

    private boolean isDDL(String sql) {
        return sql.startsWith("create") || sql.startsWith("alter") || sql.startsWith("comment");
    }

    private boolean isBasic(String sql) {
        return sql.startsWith("select") || sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete");
    }

}
