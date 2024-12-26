# P6spy 적용

## 기본 JAP SQL Logging 설정
- 간단하게 JPA 설정 시 한번에 설정 가능

### applicaion.yml
```yaml
  ## JPA setting
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        # ✔ 해당 옵션을 사용해야 format이 설정되어 보기 좋게 나옴
        format_sql: true
    show-sql: true
```

### Result Console
```shell
Hibernate: 
    insert 
    into
        member
        (name, id) 
    values
        (?, ?)
```

## P6spy SQL Logging 설정

```properties
# ✅ 기본 Dependencies 추가 만으로도 "?"로 표시 되던 qeury가 파라미터가 표시되어 나오지만
#     one line으로 표시 되기에 추가적인 설정이 필요하다.
```

### Dependencies 추가

```groovy
dependencies {
	// p6spy
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'
}
```
### applicaion.yml
- 기존 logging 설정 제거
```properties
  ## JPA setting
  jpa:
    hibernate:
      ddl-auto: update
```

### P6spy Custom format 정의 Class
```java
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
```

### P6spy 설정
- Custom P6spy 설정 파일을 지정 
- `@EventListener`
  - 애플리케이션이 완전히 초기화되거나 리프레시된 후에 특정 작업을 실행할 수 있게 해줌
  - `@PostConstruct`를 사용 할 수 있지만 성능상 해당 어노테니션이 좋을 수 있으나 규모가 클 경우 문제가 생길 수 있음
    - 의존성 주입이 완료된 후 지정 메서드 실행
     

```java
@Log4j2
@Configuration
public class P6spyLogMessageFormatConfig {
    // 애플리케이션이 완전히 초기화되거나 리프레시된 후에 특정 작업을 실행할 수 있게 해주는 Spring의 이벤트 리스너 애너테이션
    @EventListener(ContextRefreshedEvent.class)
    public void configureP6SpyLogging() {
        String formatClassName = CustomP6spySqlFormat.class.getCanonicalName();
        log.info("P6Spy Log Message Format set to: {}", formatClassName);
        P6SpyOptions.getActiveInstance().setLogMessageFormat(formatClassName);
    }
}
```
