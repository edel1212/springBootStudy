<h1>Spring Boot 빌드 및 설정 등</h1>

<h3>1 . Gradle , Maven 차이</h3>

- Spring Boot Project를 생성시 빌드 도구 결정이 필요하다.</br> 여기서 위에서 언급한
  <b  style='color:blue'>Gradle</b> 과 <b  style='color:orange'>Maven</b>은 Spring Project의 빌드 도구이다.

- 빌드 도구❔
  - Project에서 작성한 Java, xml, propterties, jar ...등등 파일들을  
    JVM, WAS가 인식 할수 있도록 패키징 해주는 것이라 할수있다.
  - 쉽게 말해 프로젝트 생성, 테스트 빌드 , 베포, 외부라이브러리 다운로드 등 을 돕는 프로그램이라 보면된다.

- <b  style='color:orange'>Maven</b>❔
  - 아파치 메이븐은 자바용 프로젝트 관리 도구이다.
  - 메이븐의 특징적인 점은 그 라이브러리들과 연관된 라이브러리들까지 거미줄처럼 모두 연동이 되서 관리가 된다는 점입니다.
  - <b>POM</b> [ Project Object Model ]을 사용한다.
    - 메이이븐은 기능을 이용하기 위해 <b>POM</b>이 사용되며 <b>pom.xml</b>에서 주요하게 다루는 내용은
      - 1 ) 프로젝트 정보 : 프로젝트의 이름, 라이센스 등
      - 2 ) 빌드 설정 : 소스, 리소스, 라이프사이클별 실행한 플로그인 등 빌드와 관련된 설정
      - 3 ) 빌드 환경 : 사용자 환경 별로 달라질 수 있는 프로파일 정보
      - 4 ) pom 연관 정보 : 의존 프로젝트(모듈), 상위 프로젝트, 포함하고 있는 하위 모듈 등

- <b  style='color:blue'>Gradle</b>❔
  - 빌드, 프로젝트 구성/관리, 테스트, 배포 도구이다.
  - 빌드 속도가 <b  style='color:orange'>Maven</b>에 비해 10~100배 가량 빠르다.
  - JAVA, C/C++M Python 등을 지원한다.
  - 빌트툴인 Ant Builder와 Groovy 스크립트 기반으로 만들어져 기존 Ant의 역할과 배포 스크립트의 기능을 모두 사용 가능하다.
    - <b  style='color:blue'>Gradle</b>은 기능을 이용하기 위해 <b>buld.gradle</b>이 사용되며 주요 기능으로는
    - 1 ) <b  style='color:blue'>Gradle</b>의 경우 별도의 빌드 스크립트를 통하여사용할 어플리케이션 버전, 라이브러리 등의 항목을 설정할 수 있습니다.
    - 2 ) Groovy 스크립트 언어로 구성되어 있기에 XML과 달리 변수선언, if, else, for 등의 로직이 구현가능하며 간결하게 구성 가능합니다.

- ✅비교 결과
  - 1 )&nbsp; 스크립트 길이와 가독성 면에서 gradle이 우세.
  - 2 )&nbsp; 빌드와 테스트 실행 결과 gradle이 더 빠르다. (gradle은 캐시를 사용하기 때문에 테스트 반복 시 차이가 더 커진다.)  
     변화가 없는건 skip 하고 빌드함!
  - 3 )&nbsp; 의존성이 늘어날 수록 성능과 스크립트 품질의 차이가 심해질 것이다.

  ```
  maven은 프로젝트가 커질수록 빌드 스크립트의 내용이 길어지고 가독성이 떨어집니다.[ xml 형식이기 때문 ]

  반면에 gradle은 훨씬 적은 양의 스크립트로 짧고 간결하게 작성할 수 있습니다.

  maven이 정적인 형태의 XML 기반으로 작성되어 동적인 빌드를 적용할 경우 어려움이 많다면,

  gradle은 Groovy를 사용하기 때문에 동적인 빌드는 Groovy 스크립트로 플러그인을 호출하거나  직접 코드를 작성하여 구현이 가능함.

  ```

<hr/>

<br/>
<h3>2 .Spring , Spring Boot 차이</h3>

- 간단하게 요약
  - <b class ="spring">Spring Boot</b> 는 <b  class ="spring">Spring</b>의 확장 버전이라보면 된다
  - <b class="spring">Spring Framework</b>에 비해서 개발, 테스트, 배포가 혁신적으로 간편해졌다.

- <b  style='color:lightgreen'>Spring Framework</b>❔
  - Spring은 Java 기반의 오픈소스 Back-End 프레임워크입니다.
  - <b>특징</b>
    - 1 ) DI(Dependency Injection) - 의존성 주입
       - DI란 개발자가 Spring 프레임워크에 의존성을 주입하면서 객체 간 결합을 느슨하게 하는 것입니다.  
    객체 간 결합이 느슨하면 코드의 재사용성이 증가하고, 단위 테스트가 용이해집니다.

    - 2 ) IoC(Invesion of Control) - 역전제어
      - IoC는 컨트롤의 제어권이 개발자에게 있는 것이 아닌 프레임워크가 대신해서 해주는 것을 말합니다.
      - Servlet이나 Bean 같은 코드를 개발자가 직접 작성하지 않고, 프레임워크가 대신 수행합니다. 제어의 역전이라는 말이 어려울 수 있는데,  
      기존에는 자바 코드를 작성할 때 객체의 생성, 의존관계 설정 등을 개발자가 해줘야 했지만, 프레임워크가 대신해준다는 의미입니다.

    - 3 ) AOP(Aspect Oriented Programming) - 관점지향 프로그래밍
      - AOP는 핵심기능을 제외한 부수적인 기능을 프레임워크가 제공하는 특징입니다. 예를 들어 Spring 프로젝트에 security를 적용하거나,  
      logging 등을 추가하고 싶을 때 기존 비즈니스 로직을 건들지 않고 AOP로 추가할 수 있습니다.
      
    - 4 ) 다른 프레임워크와의 통합
      - JUnit, Mockito와 같은 유닛 테스트 프레임워크와 통합이 간단합니다. 이를 통해 개발하는 프로그램의 품질이 향상됩니다.

- <b>차이점</b>❔
  - 1 ) Dependency
  - <b  style='color:lightgreen'>Spring Framework</b>
  - ㄴ> dependency 설정 파일이 매우 길고 모든 dependency에 대한 버전 관리 또한 관리를 해줘야한다. **[ 버전들간의 호환성 문제로 버전을 찾아야 하는 번거로움이 있음 ]**

  ```xml
  <!-- pom.xml -->

  <dependencies>
  <!-- Spring Test -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>5.0.7.RELEASE</version>
    </dependency>

    <!-- MariaDB JDBC-->
    <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
      <version>2.6.2</version>
    </dependency>

    <!-- [connectionPool]https://mvnrepository.com/artifact/com.zaxxer/HikariCP -->
  	<dependency>
  		<groupId>com.zaxxer</groupId>
  		<artifactId>HikariCP</artifactId>
  		<version>3.4.5</version>
  	</dependency>


    <!-- 등등 .. 쭉쭉 이어져 나간다.. -->
  <dependencies>
  ```

  - <b  style='color:lightgreen'>Spring Boot Framework</b>
    - ㄴ> boot:spring-boot-starter을 사용하여 상속을 통해 쉽게 관련 dependency 설정이 가능하고 무엇보다.  
    자주 사용되는 dependncy 의 경우 호환 버전이 자동으로 설정되어있어 버전 관리에 신경을 안쓰고 개발이 가능하다.

  ```gradle
  //build.gradle

  dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    annotationProcessor 'org.projectlombok:lombok'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // test 환경 lombok 사용 가능하끔 설정
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'

  }
  ```

  - 2 ) 간편한 설정 및 내장 Tomcat
    - <b  style='color:lightgreen'>Spring Framework</b>
      - ㄴ> servlet-context.xml에서 context:component-scan 을 Controller Package 지정 필요
      - root-context.xml에서 context:component-scan DI 주입에 필요한 각각의 pacakge 등록 필요
      - 따로 Tomcat을 설치하여 사용해야함.
      - <b  style='color:lightgreen'>Spring Boot Framework</b>
      - ㄴ> <b>@SpringBootApplication</b> [ AutoConfiguration ] 를 사용하여 외부의 라이브러리, 내장 톰켓 DI 주입에  
      필요한 객체들을 어노테이션을 자동으로 스캔하여 Bean Container에 등록해 줌.

```java
//java - ProjectApplication.java

  @SpringBootApplication
  public class BimovieApplication {
    public static void main(String[] args) {
  	  SpringApplication.run(BimovieApplication.class, args);
    }
  }

  //////////////////////////////////////////////////
  //@SpringBootApplication 내부
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Inherited
  @SpringBootConfiguration
  @EnableAutoConfiguration
  @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),  //ComponentScan 해줌!!
  @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
  public @interface SpringBootApplication {
    // ...Code...
  }
  ```

  - 3 ) 간편한 배포
    - <b  style='color:lightgreen'>Spring Framework</b>
    - ㄴ> WAR [Web Application Archive]를 사용하여 Tomcat, Weblogic, Websphere 등 웹 서버 또는 WAS에 올려 배포 해야함.
    - <b  style='color:lightgreen'>Spring Boot Framework</b>
    - ㄴ> 내장 WAS 를 가지고 있기 떄문에 JAR [Java Archive] 만으로도 쉽게 배포가 가능함.

<hr/>

<h3>3 . <b  style='color:lightgreen'>Spring Boot</b> Port 변경 방법</h3>

> src -> main -> resoucre -> application.properties

```properties
# application.properties

#Port 변경  server.port = ?? 로 변경 가능
server.port = 8081
```

<hr/>

<h3>4 . <b  style='color:lightgreen'>Spring Boot</b> JUnit Test 방법</h3>
- 기본적으로 Spring boot의 경우 기본적으로 dependency에 <b>org.springframework.boot:spring-boot-starter-test</b>가     
추가되어 있어 있어 간단한 설정만으로도 테스트가 가능하다.

🎈 Junit Test 시 Lombok을 사용하려면 dependencies 추가 설정이 필요하다

```yaml
# build.gralde
dependencies {
   #code..
   # test 환경 lombok 사용 가능하끔 설정
 testCompileOnly 'org.projectlombok:lombok'
 testAnnotationProcessor 'org.projectlombok:lombok'
}

```

🎈 기본적으로 service 관련 기능 테스트를 하고싶으면 Main source의 Pakcage 명을 맞춰서 진행 해주자!
<br/>

- ex ) 
  - [ Main Code ] : src -> main -> 'pakcageDir..' -> repository -> JUnitRepository.java일 경우 
  - [ Test Code ] : src -> test -> 'pakcageDir..' -> repository -> JUnitRepositoryTests.java

<br/>
❌ 주의사항

- 대상 파일명과 테스트 파일명이 같지 않게 조심하자. 
- Bean에 등록된 객체의 DI 주입은 Autowired를 사용해야한다
- Bean에 등록되지 않은 객체는 @BeforeEach를 사용하여 객체를 생성해 줘야한다.

```java
// JUnit Test Code...

//해당 Class는 compoent-sacn 대상이 아님
@SpringBootTest  // Test Class 임을 지정
@Log4j2          // log
public class TimeTests {

  //compoent-scan 대상이 아님
  private JWTUtil jwtUil;

    @BeforeEach
    public void testBefore(){
        log.info("testBefore!!!!");
        jwtUil = new JWTUtil();
    }

    @Test // 해당 어노테이션을 달아줘야 단위 테스트가 가능함
    public void getTimeTest(){
        log.info("Hello World");
    }
}
```
