# Log

```properties
# ✅ DB 및 P6psy 설정이 되어있다는 기반으로 시작
```
- 참고 :  **[ [이동](https://github.com/edel1212/springBootStudy/tree/main/p6spyStudy) ]**  

## logback-spring.xml

```properties
# ✅ resources 하위 logback-spring.xml 파일을 생성 후 내용 작성 필요
```

## 로그 설정
- `<root level="INFO">`
  - `root`의미
    - 특정 로거(패키지나 클래스 로거)가 설정되어 있지 않으면 루트 로거가 대신 로그를 처리함
  - `level="INFO"` 의미
    - 기본적으로 애플리케이션의 로그를 과도하지 않으면서도 중요한 정보를 제공하도록 설정하는 일반적인 운영 환경의 기본값
- 로그 레벨의 우선순위
  - TRACE < DEBUG < INFO < WARN < ERROR
    - TRACE:
      - 가장 낮은 레벨로, 매우 상세한 디버깅 정보를 기록.
    - DEBUG:
      - 개발 및 디버깅 시 유용한 정보를 기록.
    - INFO: 
      - 일반적인 애플리케이션 실행 흐름에 대한 정보를 기록.
    - WARN: 
      - 주의가 필요한 상황을 기록(잠재적 문제).
    - ERROR: 
      - 오류 상황을 기록(애플리케이션이 실행될 수 없는 경우).
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- Define the console appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <charset>UTF-8</charset>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) %boldWhite([%C.%M:%yellow(%L)]) - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
    
</configuration>
```

## Profile - Log 설정
- Application Profile Level에 맞는 Log 설정
- `springProfile name=" Profile Level "`
  - Profile Level에 맞는 `appender` 사용

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

  <!-- Define the console appender -->
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <charset>UTF-8</charset>
      <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) %boldWhite([%C.%M:%yellow(%L)]) - %msg%n</pattern>
    </encoder>
  </appender>

  <!-- Define logging for 'test' profile -->
  <springProfile name="test">
    <root level="INFO">
      <appender-ref ref="CONSOLE"/>
    </root>
  </springProfile>

  <!-- Define logging for 'local' profile -->
  <springProfile name="local">
    <root level="INFO">
      <appender-ref ref="CONSOLE"/>
    </root>
  </springProfile>

  <!-- Define logging for 'dev' profile -->
  <springProfile name="dev">
    <root level="INFO">
      <appender-ref ref="CONSOLE"/>
    </root>
  </springProfile>
  
  <!-- Define logging for 'prod' profile -->
  <springProfile name="prod">
    <!--  File Drop  -->
  </springProfile>

</configuration>
```

## Log 파일 생성 


### 설정 properties 파일
```properties
# ✅ xml에서 설정 값을 불러오기 위해서는 yaml이 아닌 properties를 사용해야 불러올 수 있다 
```
- Log File 생성 디렉토리 위치 및 파일명 지정
  - `${변수명}`을 사용하여 `logback-spring.xml`에서 사용 

#### logback-variables.properties
```properties
LOG_PATH=C:/Users/logDir

LOG_FILE_NAME=log-file
```

### logback 설정

#### logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

  <!-- Define the console appender -->
  <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <charset>UTF-8</charset>
      <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) %boldWhite([%C.%M:%yellow(%L)]) - %msg%n</pattern>
    </encoder>
  </appender>

  <!-- ✔ properties 를 사용하여 공통 값을 불러옴 -->
  <property resource="logback-variables.properties"/>
  <!-- ✔ 오늘 날짜를  생성하는 변수 생성  -->
  <timestamp key="ToDay" datePattern="yyyyMMdd" />

  <!-- ✔ 로그 파일 생성 기능 appender 생성  -->
  <appender name="LEVEL-INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <filter class="ch.qos.logback.classic.filter.LevelFilter">
      <level>info</level>
      <onMatch>ACCEPT</onMatch>
      <onMismatch>DENY</onMismatch>
    </filter>
    <file>${LOG_PATH}/abc/${ToDay}_${LOG_FILE_NAME}.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
      <!-- Specify the file pattern with %d for date and %i for indexing -->
      <fileNamePattern>${LOG_PATH}/abc/%d{yyyyMMdd}_${LOG_FILE_NAME}_%i.log</fileNamePattern>
      <!-- Set the maximum file size for each log file -->
      <maxFileSize>600MB</maxFileSize>
      <!-- Retain logs for 30 days -->
      <maxHistory>30</maxHistory>
    </rollingPolicy>
    <encoder>
      <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>

  <root level="INFO">
    <appender-ref ref="CONSOLE"/>
    <!--  ✅ Log File 생성 appender 사용  -->
    <appender-ref ref="LEVEL-INFO"/>
  </root>

</configuration>
```

### xml include 파일

#### include 파일
- `<filter class="ch.qos.logback.classic.filter.LevelFilter">`를 사용해 Log Level을 필터링 함

##### All Log file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
    <property resource="logback-variables.properties" />
    <timestamp key="ToDay" datePattern="yyyyMMdd" />

    <appender name="LEVEL-ALL" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/all/${ToDay}_${LOG_FILE_NAME}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/all/%d{yyyyMMdd}_${LOG_FILE_NAME}_%i.log</fileNamePattern>
            <maxFileSize>600MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
</included>
```

##### Info Level Log file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
    <!--  Set Variable  -->
    <property resource="logback-variables.properties"/>
    <!--  Set Date  -->
    <timestamp key="ToDay" datePattern="yyyyMMdd" />

    <appender name="LEVEL-INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>info</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <file>${LOG_PATH}/info/${ToDay}_${LOG_FILE_NAME}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- Specify the file pattern with %d for date and %i for indexing -->
            <fileNamePattern>${LOG_PATH}/info/%d{yyyyMMdd}_${LOG_FILE_NAME}_%i.log</fileNamePattern>
            <!-- Set the maximum file size for each log file -->
            <maxFileSize>600MB</maxFileSize>
            <!-- Retain logs for 30 days -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
</included>
```

### logback - xml include
- ` <include resource="대상 xml 파일 경로" />`를 사용해서 xml 설정을 불러올 수 있다

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- Define the console appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <charset>UTF-8</charset>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-4relative] %green([%thread]) %highlight(%-5level) %boldWhite([%C.%M:%yellow(%L)]) - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Define logging for 'test' profile -->
    <springProfile name="test">
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <!-- Define logging for 'local' profile -->
    <springProfile name="local">
        <!-- Log To File Setting xml   -->
        <include resource="log/local/file-info.xml" />
        <include resource="log/local/file-warn.xml" />
        <include resource="log/local/file-error.xml" />
        <include resource="log/local/file-all.xml" />
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="LEVEL-INFO"/>
            <appender-ref ref="LEVEL-WARN"/>
            <appender-ref ref="LEVEL-ERROR"/>
            <appender-ref ref="LEVEL-ALL"/>
        </root>
    </springProfile>

</configuration>
```
