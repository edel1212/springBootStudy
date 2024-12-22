# springBootStudy


- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/spring-boot-basic) ]** :: 코드로 배우는 스프링 부트
  - JPA
  - query dsl
  - spring security
  - thymeleaf

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/exceptionStudy) ]** :: 예외 처리
  - Global Exception Handler
  - Filter 내 Exception 처리

<hr/>

- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/exGraphQL) ]** :: GraphQL 사용 방법 및 RESTAPI 차이점
  - 1 ) GraphQL 이란❔
  - 2 ) Rest API 와 GraphQL의 차이
  - 3 ) GraphQL의 장단점
  - 4 ) Spring For GraphQL 설정 및 사용 방법

<hr/>


- 💬 **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/webSocketStudy) ]** :: WebSocket
  - 1 ) WebSocket
  - 2 ) SocketJS

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
<hr/>

### Jar 파일 생성 후 배포 방법

#### 완성된 project를 jar로 배포하는 방법
- 1 . gradle -> Project -> Task -> build -> bootJar
  - jar파일을 생성해준다.
    - jar 생성 경로 
      -  project -> build -> libs 
- 2 . 해당 경로 이동
- 3 . `java -jar fileName.jar`를 통해 실행


#### jar파일 생성 시 파일명 변경 방법
- build.gradle을 수정하여 변경 가능

```groovy
plugins {/** code ..*/}

group = 'com.yoo'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {/** code ..*/}

repositories {/** code ..*/}

dependencies { /** code ..*/}

// 해당 부분을 추가
bootJar{
	archivesBaseName = '파일명지정'
	archiveFileName = '파일명지정.jar'
    archiveVersion = "0.0.0"
}

tasks.named('test') {/** code ..*/}

```


<hr/>

### Docker를 이용한 배포 시 주의사항

#### 문제사항
- DB 연결이 되지 않는 문제가 발생하였다.
  - 기존 DB URL : `spring.datasource.url=jdbc:mariadb://localhost:3303/login`

- 원인 
  - 첫번째 
    - Mariadb의 경우 Docker를 사용하여 컨테이너로 올려 사용중이였다. 하지만 SpringBoot 어플레이케이션 또한 컨테이너로 올리게 되면  
    각각 컨테이너터 간의 연결이 필요할 경우 `dorcker network`를 생성하여 `bridge`방식으로 연결을 헤줘야한다. 
      - 😅 **하지만** --network 옵션을 통해 각각의 docker image를 연결해 주었지만 인식을 못하는 문제가 발생하였다.
        - Ex) `docker run -d -p 3303:3303 --name db --network back-net mariadb`  ## DB 컨테이너 생성  
        `docker run -d -p 8801:8801 --namd backServer --network back-net 이미지`  ## Server 컨테이너 생성
      - ✅ **이유**
        - 기존에 DB설정을 `localhost:3303`이 아닌 내가 생성한 network의 연결된 DB의 IP 혹은 Bridge의 IP를 연결해줘야한다.
        - 👉 **컨테이너와 컨테이너의 연결에는 반드시 브릿지가 필요**하기 떄문임
          - ip 확인
            - `docker inpect bridge` 혹은 `docker inspect 내가 만든 네티워크` 

✅ 변경된 DB URL  
```properties

server.port = 8081

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

### bridge IP
spring.datasource.url=jdbc:mariadb://172.17.0.1:3303/login

### 내가만든 Network IP - 둘다 접근 된느것 확인 완료
##spring.datasource.url=jdbc:mariadb://172.21.0.1:3303/login

### 운영
###spring.datasource.url=jdbc:mariadb://localhost:3303/login

```