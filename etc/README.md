# ETC 

## 1 ) @JsonNaming
```properties
# ✅ Jackson 라이브러리에서 제공하는 어노테이션이다.
#     객체 직렬화(Serialization) 및 역직렬화(Deserialization) 시 속성 이름의 변환 규칙을 지정하는 데 사용된다.
```
- JSON 데이터의 속성 이름 형식(예: snake_case, camelCase)을 클래스 필드와 다르게 지정해야 할 때 사용
### 네이밍 전략 종류

- **SNAKE_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)`
  - 변환 예시: `firstName` → `first_name`

- **UPPER_SNAKE_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)`
  - 변환 예시: `firstName` → `FIRST_NAME`

- **LOWER_CAMEL_CASE** *(기본값)*
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)`
  - 변환 예시: `firstName` → `firstName`

- **UPPER_CAMEL_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)`
  - 변환 예시: `firstName` → `FirstName`

- **LOWER_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)`
  - 변환 예시: `firstName` → `firstname`

- **KEBAB_CASE**
  - 어노테이션: `@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)`
  - 변환 예시: `firstName` → `first-name`



### 설정 방법
- 응답 값
  - `{"AppleID":"BlackGom","FlagBool":true,"NumberCd":100,"SakuraNum":"Hahahah100"}`
    - **Key 값**이 **대문자로 시작**하는 `UpperCamelCaseStrategy`형태
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class UpperDTO {
private String AppleID;

    private String SakuraNum;

    private Boolean FlagBool;

    private Long NumberCd;

}
```

## 2 ) Dependencies 스코프 관련

- `Test Scope`로 설정 시 프로젝트가 실행 시 해당 디팬던시는 사용되지 않는다.
  - 필요 경우에 따라 **스코프를 변경하여 개발하는 습관**을 들이자.
    - 테스트에 사용할 DB는 스코프를 그에 맞게 변경
- ex) h2 Database의 경우 테스트로만 사용 예정
  - Maven
    ```xml
    <deepndency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <!--    <scope>runtime</scope> -->
      <!-- 변경 -->
      <scope>test</scope>
    </deepndency>
    ```
  - Gradle
    ```groovy
    dependencies {
    //runtimeOnly 'com.h2database:h2'
    /** 변경  */
    testImplementation 'com.h2database:h2'
    }
    ```
    | 스코프 | Maven 설정 | Gradle 설정 | 설명 |
    |-----------------|--------------------------------------|--------------------------------------------|--------------------------------------------|
    | 컴파일 | `<scope>compile</scope>` | `implementation 'group:artifact:version'` | 모든 빌드 단계에서 사용되는 기본적인 의존성 범위 |
    | 컴파일 전용 | `<scope>provided</scope>` | `compileOnly 'group:artifact:version'` | 컴파일 시에만 사용되며, 런타임에서는 제외됨 |
    | 런타임 전용 | `<scope>runtime</scope>` | `runtimeOnly 'group:artifact:version'` | 런타임 시에만 사용되며, 컴파일 시에는 사용되지 않음 |
    | 시스템 | `<scope>system</scope>` | 사용하지 않음 (사용 시에는 추가 설정 필요) | 시스템에 직접 설치된 JAR 파일과 같은 외부 JAR 파일에 대한 의존성 |
    | 테스트 | `<scope>test</scope>` | `testImplementation 'group:artifact:version'`| 테스트 코드에서만 사용되는 의존성 |
    | 어노테이션 프로세서 | `<scope>compile</scope>` + 어노테이션 프로세서 플러그인 | `annotationProcessor 'group:artifact:version'` | 컴파일 시에만 사용되는 어노테이션 프로세서 |

## 3 ) Entity 생성

- Java Bean 스팩에 맞게 구현하며,  빌더 패턴만 사용하게 끔 하지 말자
- `@Builer`는 매개변수가 없는 **디폴트 생성자를 생성** 메서드를 **만들어 주지 않는다.**
  - 따라서 `@NoArgsConstructor`를 사용하라면 `@AllArgsConstructor`는 항상 같이 따라 다닌다 보면 된다.
- `@EqualsAndHashCode`를 사용하면 `StackOverFlow`가 생길 수 있는 일을 미연에 방지가 가능하다.
  - 지정한 값을 기준으로 `Entity`간의 비교가 가능해지기 떄문
  - Set형태로 여러개 지정이가능하다
- `@Data`를 사용하지 않는 이유 또한 위와 같은 이유이다 **EqualsAndHashCode**를 모든 필드로 만들어 버림
  - 다른 Entity를 `상호참조`로 인해 `StackOverFlow`가 발생 할 수 있다.

- ### Entity 코드
  ```java
  @Builder 
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  /**
  * 지정한 값을 기준으로 entity간의 비교가 가능해짐
  * - 사용을 하지 않으면 모든 값을 기준으로 비교하는데 이떄 "상호 참조"떄문에 stackoverflow가 발생할 수 도 있음
  * - 원한다먄 Set 형태로도 여러개의 비교 값을 지정이 가능함
  *   - ex) ( of = {"id", "name"})
  * ✨ 여기서도 중요한건  stackoverflow가 발생하지 않게 EqualsAndHashCode에는
  *    다른 Entity를 참조하는 필드를 넣지 않느 것이다.
  * */
  @EqualsAndHashCode( of = "id")
  // 😱 @Data  <<가 있지만 Entity에서는 사용하지말자 위에서 말한 EqualsAndHashCode를 모든 필드를 대상으로 만들기 떄문이다.
  public class Event {
    private Integer id;
    private boolean offline;
    /** code.. */
  }
  ```

## 4 ) ModelMapper

- `DTO -> Entity` 혹은 `Entity -> DTO`와 같은 변환에 유용한 **라이브러리**이다

- 사용 방법
  - 의존성 추가
    - `implementation group: 'org.modelmapper', name: 'modelmapper', version: '3.2.0'`
  - Bean 등록
    ```java
    @SpringBootApplication
    public class RestApiApplication {
      // Application Start Code 생략 ...
      @Bean
      public ModelMapper modelMapper(){ return new ModelMapper(); }
    }
    ```
  - 비즈니스 로직 사용
    -  DTO -> Entity 변환
    - `Event event = modelMapper.map(eventDTO, Event.class);`

- ### ModelMapper - 사용 시 TestCode 문제
  - 이슈 사항
    - `@WebMvcTest` 테스트는 웹 관련 빈만 등록하기 떄문
      - 다른 빈이 필요하면 직접 추가해야 함
      - 특히, 테스트 시 ModelMapper와 같은 빈은 자동으로 주입되지 않는다
      - `@MockBean`을 통해 사용하여 필요한 객체를 주입이 가능하나 번거롭다.

  - 해결 방법
    - 테스트 시 전체 프로세싱으로 테스트한다 (통합 테스트 환경)
      - `@SpringBootTest,@AutoConfigureMockMvc`를 통해 스터빙을 끊고 자동으로 전체 주입 받는 형식으로 구현

  - 🤔 고민 했던 문제
    - 위와 같이 통합 테스트 환경으로 테스트를 진행하는게 맞는지 고민함
      - 강의에서는 웹쪽 관련 테스트 코드는 해당 방법일 선호하며 추천한다고 함
        - 웹 테스트 시 많은 모킹이 필요여 번거롭고 관리 또한 힘들다
          - 코드가 바뀌면서 전체적인 설정이 바뀔 수도 있고 그에 따라 위험도가 올라가는 문제 또한 있다
        - 시간이 많이 들며 그럴 바에는 차라리 전체 빈을 주입받아 테스트하는 것이 효율적이라 하였다.
    - `@SpringBootTest`를 사용하면 Application에 설정되어있는 모든 빈을 주입하여 테스트하며 실제 어플리케이션과 가장 가까운 형태로 테스트가 가능하다.
      - API 테스트 시 슬라이싱 테스트 보다 해당 방법을 선호한다.


## 5 ) properties 설정 Class Mapping
```properties
# ℹ️ 코드 내 작성되어 있던 문자열을 `properties`에 작성 후 Class에 매핑
#    ㄴ> 가독성이 좋아지며 한번에 관리하기도 좋아짐
```
- ### dependencies
  - `annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'`

- ### application.properties
  ```properties
  my-app.username = "edel1212"
  my-app.password = "123"
  ```
- ### MappingClass
  - `@ConfigurationProperties(prefix = "my-app")` 설정을 하면 시작 명칭으로도 가능
  ```java
  @Component
  @ConfigurationProperties("my-app")
  @Getter
  @Setter
  public class AppProperties {
      @NotNull
      private String username;
      @NotNull
      private String password;
  }
  ```
- ### Test Code
  ```java
  @SpringBootTest
  public class AppPropertiesTests {
  
      @Autowired
      private AppProperties properties;
  
      @Test
      void name() {
          System.out.println("userName  :: " + properties.getUsername());
          System.out.println("userPassword  :: " + properties.getPassword());
      }
  }
  ```
