package com.yoo.p6spy.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.util.StringUtils;

@Log4j2
public class CustomP6spySqlFormat implements MessageFormattingStrategy {

    /**
     * SQL 쿼리 실행 후 로그 메시지를 포맷합니다.
     *
     * @param connectionId 쿼리의 연결 ID
     * @param now 쿼리가 실행된 현재 시간
     * @param elapsed 쿼리 실행 시간(밀리초 단위)
     * @param category 쿼리의 카테고리 (예: 'SELECT', 'INSERT')
     * @param prepared 준비된 SQL 문
     * @param sql 실행된 SQL 쿼리
     * @param url 데이터베이스 연결 URL
     * @return 카테고리, 실행 시간, 포맷팅된 SQL을 포함한 포맷된 문자열
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        // SQL이 비어 있거나 null인 경우 카테고리와 실행 시간만 반환
        if (!StringUtils.hasText(sql))  return String.format("%s %dms", category, elapsed);
        // SQL이 유효한 경우 카테고리, 실행 시간, 포맷팅된 SQL 반환
        return String.format("%s %dms %s", category, elapsed, highlight(format(sql)));
    }

    /**
     * SQL 쿼리를 그 종류에 맞게 포맷합니다.
     *
     * @param sql 포맷할 SQL 쿼리
     * @return SQL 쿼리 유형에 맞게 포맷팅된 SQL 쿼리
     */
    private String format(String sql) {
        if (isDDL(sql)) {
            return FormatStyle.DDL.getFormatter().format(sql);
        } else if (isBasic(sql)) {
            return FormatStyle.BASIC.getFormatter().format(sql);
        }
        return sql;
    }

    /**
     * SQL 쿼리를 하이라이트 처리합니다.
     *
     * @param sql 하이라이트할 SQL 쿼리
     * @return 하이라이트된 SQL 쿼리
     */
    private String highlight(String sql) {
        return FormatStyle.HIGHLIGHT.getFormatter().format(sql);
    }

    /**
     * 주어진 SQL 쿼리가 DDL(데이터 정의 언어) 쿼리인지 확인합니다.
     *
     * @param sql 확인할 SQL 쿼리
     * @return DDL 쿼리일 경우 true, 아니면 false
     */
    private boolean isDDL(String sql) {
        return sql.startsWith("create") || sql.startsWith("alter") || sql.startsWith("comment");
    }

    /**
     * 주어진 SQL 쿼리가 기본 SQL 쿼리인지 확인합니다.
     *
     * @param sql 확인할 SQL 쿼리
     * @return SELECT, INSERT, UPDATE, DELETE 쿼리일 경우 true, 아니면 false
     */
    private boolean isBasic(String sql) {
        return sql.startsWith("select") || sql.startsWith("insert") || sql.startsWith("update") || sql.startsWith("delete");
    }

}
