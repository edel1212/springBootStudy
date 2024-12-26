package com.yoo.mybatis.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.springframework.util.StringUtils;


public class CustomP6spySqlFormat  implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        // SQL이 비어 있거나 null인 경우 카테고리와 실행 시간만 반환
        if (!StringUtils.hasText(sql))  return String.format("%s %dms", category, elapsed);
        return String.format("%s %dms %s", category, elapsed, format(sql));
    }

    private String format(String sql) {
        return sql;
    }

}
