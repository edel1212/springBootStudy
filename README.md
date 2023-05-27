# springBootStudy

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/ex01) ]** :: Spring Boot, Spring Framework 차이 및 설정 JUnit Test
  - 1 ) Gradle , Maven 차이
  - 2 ) Spring , Spring Boot 차이
  - 3 ) Spring Boot Port 변경 방법
  - 4 ) Spring Boot JUnit Test 방법

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/ex02) ]** :: JAP, ORM, JPQL, Paging 및 DB설정 Docker를 사용 하여 DataBase 사용법
  - 1 ) ORM [ 객체 관계 매핑 (Object–relational mapping) ]❔
  - 2 ) JPA(Java Persistence API)❔
  - 3 ) JPA, DB 추가 및 DB접속 정보 설정
  - 4 ) Entity Class란❔
  - 5 ) JpaRepository란❔
  - 6 ) JpaRepository를 사용한 CRUD
  - 7 ) JpaRepository를 사용한 Paging
  - 8 ) QueryMethod
  - 9 ) @Query 어노테이션 [ JPQL ]
  - 10 ) Docker를 사용한 MariaDB 설치 및 연결
<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/ex03) ]** :: Thymeleaf 사용법, RedirectAttributes를 사용하여 Redirect 시  데이터 전달
  - 1 ) Thymeleaf 란❔
  - 2 ) Thymeleaf를 사용해 데이터 출력 [ 반복문 및 조건문]
  - 3 ) Thymeleaf를 사용해 데이터 출력 [ inline ]
  - 4 ) Thymeleaf의 링크 처리 및 레이아웃

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/ex04) ]** :: REST API, Swagger, RestTemplate, WebClient 사용 방법
  - 1 ) REST API 란❔
  - 2 ) @RestController 란❔
  - 3 ) @RequestMapping의 Consumes 와 Produces 란❔
  - 4 ) Content-Type의 application/json 과 application/x-www-from-urlencoded 차이점❔
  - 5 ) Swagger Setting
  - 6 ) RestTemplate 이란 ?
  - 7 ) WebClient 란❔

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/exGraphQL) ]** :: GraphQL 사용 방법 및 RESTAPI 차이점
  - 1 ) GraphQL 이란❔
  - 2 ) Rest API 와 GraphQL의 차이
  - 3 ) GraphQL의 장단점
  - 4 ) Spring For GraphQL 설정 및 사용 방법

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/board) ]** :: N:1(다 : 1) 연관관계(Join 사용), @Query, querydsl(JPQLQuery 객체)
  - 1 ) @ManyToOne 연관관계 ❔
  - 2 ) 지연로딩(lazy Loading)의 장단점❔
  - 3 ) JPQL 과 left (outer) join
  - 4 ) Object[]를 DTO로 변경하기 - [ JPQL Pageing 반환 ]
  - 5 ) 연관관계가 있을 경우의 삭제
  - 6 ) QuerydslRepositorySupport의 JQPLQuery 사용 Join 및 Object[] 다루기
    - QuerydslRepositorySuppoert은 2가지 방법이 존재( JPAQueryFactory, JPQLQuery ) 차이가 있음 

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/guestbook) ]** :: MappedSuperClass , Querydsl, 페이징 처리
  - 1 ) MappedSuperClass 란❔
  - 2 ) Querydsl ❔
  - 3 ) 서비스 계층과 DTO ❔
  - 4 ) 페이징 처리 DTO 사용
  - 5 ) @ModelAttribute 란❔

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/mreview) ]** :: M : N(다 대 다) [@EntityGraph] , 파일업로드 불러오기
  - 1 ) M : N(다 대 다) 관계의 특징
  - 2 ) M : N(다 대 다) 연관관계를 만들시 문제점
  - 3 ) M : N(다 대 다) 연관관계 해결방안 [ Row를 사용 수직으로 확장하면 된다! ]
  - 4 ) 매핑 테이블의 특징 ?
  - 5 ) JPA에서 M:N(다대다) 처리 방법 - 사전 준비
  - 6 ) 상단에 명시된 테스트 목록 테스트
  - 7 ) 파일 업로드 처리
<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/club) ]** :: Spring Security, 소셜 로그인, JWT
  - 1 ) Spring Security
  - 2 ) Spring Security - *DB사용* [ 설정 및 설명 ]
  - 3 ) Spring Security - DB사용 [ 사용 코드 및 흐름 ]
  - 4 ) Security 정보 확인 Client , Server
  - 5 ) @PreAuthorize 란 ?
  - 6 ) 로그인 실패 시 설정 [ FailureHandler ]
  - 7 ) 로그인 성공 시 설정 [ SuccessHandler ]
  - 8 ) OAuth Login [ 소셜 로그인 ]
  - 9 ) JWT (JSON Web Token)

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/integrateVue) ]** :: Spring Boot Vue 연동 라우터 사용

<hr/>

### Mybatis 사용 방법

- 1 . dependencies 내 Mybatis 추가
- 2 . application.properties 파일 내 DB 설정 및 Mybatis 경로 추가
```properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3306/bootex
spring.datasource.username=root
spring.datasource.password=123

## Mybatis Setting ##
## 👉 사용될 패키치 위치를 지정해준다
mybatis.mapper-locations = classpath:mapper/**/*.xml
```

- 3 . Mybatis를 읽을 Interface 추가
  -  👉 중요사항 : 인터페이스는 properties에 지정한 패키지명, 또는 위치에 맞게 생성하지 않아도 괜찮다
    - 진짜로 위치를 읽는것은 .xml 내부 `namespace`이다 
    - 단 가독성을 위해 맞게 위치하는 것이 좋다. src - > main -> myRoot -> mapper  
      - 경로 수정을 통해 하위 패키지 내부에 지정 가능함
```java
package com.yoo.instarServer.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Testyoo {
    String getName();
}

```
- 4 . resources 내부 xml 생성
  - 파일명이 소문자여도 알아서 인식해줌 (단 소문자여도 인터페이스명과는 같아야함) 
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.yoo.instarServer.mapper.Testyoo">
    <select id="getName" resultType="string">
        SELECT 'yoojh' as name FROM dual
    </select>
</mapper>
```

#### Mybatis 읽을 수 있는 흐름


- 단위 테스트 기준으로 설명


- 1 .  Mapper Interface 호출
  -  testyoo의 getName()를 호출함

✅ JUnit Test
```java
@SpringBootTest
public class mybatisTest {
    @Autowired
    private Testyoo testyoo;

    @Test
    public void test1(){
        System.out.println(testyoo.getName());
    }
}
```

- 2 .  Mapper Interface 내부 `@Mapper`를 통해 빈등록이 되어 있음 읽음
  - 사실상 파일 위치나 파일명은 중요하지 않음 mybatis를 사용하는 xml 내부 `namesapce`에만 맞으면 된다.
  
✅ Mapper Interface
```java
  @Mapper
  public interface Testyoo {
      String getName();
  }
```

- 3 .  Mybatis 내 `namespace`의 경로와 매칭되는 xml을 불러서 사용

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.yoo.instarServer.mapper.Testyoo">
    <select id="getName" resultType="string">
        SELECT 'yoojh' as name FROM dual
    </select>
</mapper>

```

<hr/>

### Mybatis Log4JDBC 사용 방법

- 1 . dependencies에 `log4jdbc`를 추가해준다.
- 2 . resources 하위 `log4jdbc.log4j2.properties`파일을 생성해준다. - 설정 파일

✅ log4jdbc.log4j2.properties
```properties
log4jdbc.spylogdelegator.name=net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator
log4jdbc.dump.sql.maxlinelength=0
```

- 3 . DB연결 설정 중 **class-name** 과 **url** 변경 및 로그 Level을 설정해준다.

✅ application.properties
```properties
## 변경 전 ❌ 
#####spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
## 변경 후 👍
spring.datasource.driver-class-name=net.sf.log4jdbc.sql.jdbcapi.DriverSpy

## 변경 전 ❌ 
#####spring.datasource.url=jdbc:mariadb://localhost:3306/bootex
## 변경 후 👍
spring.datasource.url=jdbc:log4jdbc:mariadb://localhost:3306/bootex

spring.datasource.username=root
spring.datasource.password=123

## Log Level Setting ###
logging.level.jdbc.sqlonly= DEBUG
logging.level.org.springframework.web = DEBUG
```
