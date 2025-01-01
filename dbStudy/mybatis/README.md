# Mybatis

## Dependencies
- `mybatis-spring-boot-starter`의 버전은 initializr를 통해 **확인**하여 **사용**
```groovy
dependencies {

	// p6spy
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'

	// Mybatis
	implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4'
	testImplementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4'
}
```

## application.yml
- Mapper의 package 위치를 지정
```properties
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/test
    username: root
    password: 123

mybatis:
  # ✅ Mapper의 package 위치 지정
  mapper-locations: classpath:mapper/**/*.xml
```

## VO(Value Object)
```java
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class MemberVO {
    private String id;
    private String name;
}
```

## Repository
- `@Mapper`를 통해 Mybatis사용 및 `Bean` **주입**
  - 
```java
@Mapper
public interface MemberRepository {
    List<MemberVO> getAllList();
    MemberVO fineById(String id);
}
```

## Mapper
- `namespace`의 위치는 사용하려는 메서드가 작성된 `@Mapper`어노테이션이 달린 **Interface**
- 각각의 id는 interface의 MethodName과 같음
  - `id == methodName`
- `resultType` VO 위치 지정
- 파라미터의 경우 `#{대상}`으로 지정하며 해당 대상의 값은 Interface의 **파라미터 명이 같다**면 **자동**으로 **매칭**된다
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.yoo.mybatis.repository.MemberRepository">
    <select id="getAllList" resultType="com.yoo.mybatis.vo.MemberVO">
        SELECT * FROM member
    </select>

    <select id="fineById" resultType="com.yoo.mybatis.vo.MemberVO">
        SELECT
            *
        FROM member
        WHERE
            id = #{id}
    </select>
</mapper>
```

## Query Log
```properties
# ✅ P6psy를 이용하여 작성
#    - 단 hibernate를 사용하지 않기에 일부 커스텀이 필요함 
```
- 참고 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/p6spyStudy) ]**

### hibernate를 제외한 P6psy format 설정
```java
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
```
