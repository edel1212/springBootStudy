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

- `Test Scope`로 설정 시 프로젝트가 실행 시 **해당 dependencies는 사용되지 않음**
  - 필요 경우에 따라 **scop를 변경하여 개발하는 습관**을 들이자.
    - 테스트에 사용할 DB는 scop를 그에 맞게 변경
### ex) h2 Database의 경우 Test 에만 사용 시
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

- Java Bean 스팩에 맞게 구현 필요
- JPA는 리플렉션을 통해 객체를 생성하기 때문에 기본 생성자가 반드시 필요 하기에 `@NoArgsConstructor`가 꼭 필요
- `@Builer`는 매개변수가 없는 **기본 생성자** 메서드를 **만들어 주지 않는다.**
  - 빌더 클래스에서 객체를 생성할 때 모든 필드를 초기화하는 생성자를 호출하기에  `@AllArgsConstructor`는 필수이다.
- `@EqualsAndHashCode`를 사용하면 `StackOverFlow`가 생길 수 있는 일을 미연에 방지가 가능하다.
  - 지정한 값을 기준으로 `Entity`간의 비교가 가능해지기 떄문
  - Set형태로 여러개 지정이가능하다
    - `of = {"id", "name"}`
- `@Data`를 사용하지 않는 이유 또한 위와 같은 이유이다 **EqualsAndHashCode**를 모든 필드로 만들어 버림
  - 다른 Entity를 `상호참조`로 인해 `StackOverFlow`가 발생 할 수 있다.

### Entity - 상속이 없을 경우
```java
@Builder 
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode( of = "id")
// 😱 @Data  <<가 있지만 Entity에서는 사용하지말자 위에서 말한 EqualsAndHashCode를 모든 필드를 대상으로 만들기 떄문이다.
public class Event {
  private Integer id;
  private boolean offline;
}
```

### Entity - 상속이 있을 경우
```java
@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
public class Memo {

    @Comment("PK")
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Comment("메모")
    @Column(length = 200, nullable = false)
    private String memoText;

}
```

## 4 ) ModelMapper

```properties
# ℹ️ `DTO -> Entity` 혹은 `Entity -> DTO`와 같은 변환에 유용한 **라이브러리** 
```

### dependencies
```groovy
dependencies{
  implementation 'org.modelmapper:modelmapper:3.2.0'
}
```
### Config Class 추가
```java
@Configuration
public class ModelMapperConfig {
  @Bean
  public ModelMapper modelMapper(){
    return new ModelMapper();
  }
}
```

### 사용 예시
-  DTO -> Entity 변환
  - `Event event = modelMapper.map(eventDTO, Event.class);`

### ModelMapper - 사용 시 TestCode 문제
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
## 6 ) @JsonUnwrapped

```properties
# ℹ️ JSON 직렬화(serialization) 및 역직렬화(deserialization) 과정에서 중첩된 객체의 필드를 
#     부모 객체의 필드처럼 평탄화(flatten) 하는 데 사용함
```

### 사용 방법
- `prefix = "foo"`를 사용할 경우 직렬화 되는 대상에 prefix가 붙는다
#### Class
```java
public class Address {
    private String street;
    private String city;
}

public class User {
    private String name;

    @JsonUnwrapped
    // @JsonUnwrapped(prefix = "addr_")
    private Address address;
}
```

#### 응답 값
- @JsonUnwrapped를 사용하지 않을 때
```javascirpt
{
  "name": "John",
  "address": {
    "street": "123 Main St",
    "city": "New York"
  }
}
```

- @JsonUnwrapped를 사용할 때:
```javascirpt
{
  "name": "John",
  "street": "123 Main St",
  "city": "New York"
}
```

## 7 ) MvcResult 결과 값 받기
```java
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("로그인_통과")
    @Test
    void login_통과() throws Exception {
        String requestMemberName = "seohae";
        // 1. MvcResult 생성 
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .param("memberName", requestMemberName)
                        .param("password", "12341234"))
                .andExpect(status().isOk())
                .andReturn();
        
        // 2. 응답 body 값을 문자열로 변환
        String response = result.getResponse().getContentAsString();
        // 3. Jackson2JsonParser 생성
        var parser = new Jackson2JsonParser();
        // 4. JSON구조에서 지정 key 값을 가져옴
        String memberName = parser.parseMap(response).get("memberName").toString();

        // 또 다른 방법
        //  String response = result.getResponse().getContentAsString();
        // String floorPlanDataId = JsonPath.parse(response).read("$.data.floorPlanData.id");

    }
}
```

## 8 ) SpringSecurity 인증 정보 가져오기
```properties
# ℹ️ 2가지 방법이 있다.
```

### SecurityContextHolder에서 받기
```java
public class TestClass{
    public void getPrincipal(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = principal.getUsername();
        String password = principal.getPassword();
    }     
}
```

### @AuthenticationPrincipal를 사용해서 받기
- `@AuthenticationPrincipal`로 주입 받는 객체는 **User를 상속한 객체여야 함**
- @AuthenticationPrincipal(experssion = "대상키") : 대상 키를 바로 뽑아 쓸 수 있음
```java
public class wrapClass{
    @GetMapping("/")
    public void test(@AuthenticationPrincipal UserAccount userAccount){ 
    }   
}
```
#### AnnotationClass 활용
- 메타 어노테이션을 지원하므로 간소화 가능
```java
@Target(ElementType.PARAMETER)      // 파라미터 형태로 사용 명시
@Retention(RetentionPolicy.RUNTIME) // 언제까지 해당 어노테이션 지정 여부 : 런타임
/**
* ✅ 접근 대상이 anonymousUser 권한이 들어올 경우 User 객체를 타고 넘어오지 않아 응답 값이
*    anonymousUser라는 문자열로 들어오기에 해당 expression의 유연함을 활용해서 적용해주자
* */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : targetKey")
public @interface CurrentUser { }
```

## 9 ) Log 컬러 추가 방법
### properties
```properties
spring.output.ansi.enabled=always
```
### yaml
```yaml
spring:
  output:
    ansi:
      enabled: always
```

## 10 ) 문자 인코딩을 강제로 지정
```properties
# ℹ️ HTTP 응답 헤더의 Content-Type에 명시된 문자 인코딩이 항상 적용되도록 강제합니다.
#  ㄴ> 예를 들어, 응답 헤더에 Content-Type: text/html; charset=UTF-8가 포함되도록 함
```
### application.yml
```yaml
server:
  servlet:
    encoding:
      charset: UTF-8    # UTF-8 인코딩을 지정
      force: true       # 요청(Request)에도 UTF-8 인코딩 강제 적용
      force-response: true  # 응답(Response)에도 UTF-8 인코딩 강제 적용
```

## 11 ) Request 검증

### 엄격한 Request 값 제한
```properties
# ℹ️ 사용 유무는 권장이 아니며 개발 상황에 맞게 사용한다.
#    ㄴ 좀 더 엄격하게 사용자의 요청값에 제한을 두는 것
```
#### properties
```properties
spring.jackson.deserialization.fail-on-unknown-properties=true
```

#### 흐름
- Client
  - Send Request To Server : `{name : "yoo",age : 100}`
- Server DTO
```java
@Getter
class RequestDTO{
    private String name;
}
```
- Result 
  - Error 발생
  - 서버가 허용하지 않는 age를 넘기므로 예외를 발생 시킴

## 11 ) Request Validation Check

### dependencies
```groovy
dependencies{
  implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

### DTO 
- `@NotEmpty, @NotNull, @Min(0), @Max(0)` 등을 사용하여 검증
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginReq {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
```

### Controller
- Parameter 내 `@Valid` 지정으로 감시 대상 설정
- DTO내 검증 기준에 맞지 않으면 BindingResult 객채 내 에러를 담고 있음
  - boolean Type
```java
public class MemberController{
    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<JwtToken>> signIn(@Valid @RequestBody LoginReq loginReq
            // ℹ️ 해당 객체에 검증 결과를 담고 있음
            , BindingResult bindingResult){
        // 값 검증
        if(bindingResult.hasErrors()) throw new InputValidException();
        // code..
        return ResponseEntity.ok().body(entityModel);
    }
}
```
### 11 - 1 ) Request Validation Check - @Component  활용
- 복잡한 예외 처리를 할 수 있는 Class 를 만들어 예외 처리
#### Validation Check Config Class
```java
@Component // Bean 등록
public class EventValidator {
    /**
     * ℹ️ 실제 검증을 처리할 Method
     * - 각각 Parmamter로 ( 검증 대상DTO, 예외를 핸들링할 객체 )
     * */
    public void validate(EventDTO eventDTO, BindingResult bindingResult){
        // 👉 최대 값을 넘는지 체크하는 로직
        if(eventDTO.getBasePrice() > eventDTO.getMaxPrice()
            && eventDTO.getMaxPrice() > 0 ){
            // 👉 rejectValue()를 통해 에러 주입 ( 필드명, 에러코드, 에러 메세지 )
            bindingResult.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            bindingResult.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
        }//if

        // 예외 처리 두번째
        LocalDateTime eventEndTime =  eventDTO.getEndEventDateTime();
        if(eventEndTime.isBefore(eventDTO.getBeginEventDateTime())){
            bindingResult.rejectValue("endEventDateTime", "wrongValue", " endEventDateTime is wrong");
        }

        /** 위와 같은 방식으로 차례차례 검증 로직을 늘려 전부 Pass해야 정상 Request로 지정 */
    }
}
```

#### Controller
-  의존성 주입을 통해 `BindingResult` 내 예러 추가
```java
public class EventController{
    // 👉 의존성 주입
    private final EventValidator eventValidator;
    
    @PostMapping(value = "/event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<JwtToken>> eventTest(@Valid @RequestBody EventDTO eventDTO
            , BindingResult bindingResult){
        
        // 👉 검증 로직으로 확인
        eventValidator.validate(LoginReq, bindingResult);
        
        // 값 검증
        if(bindingResult.hasErrors()) throw new InputValidException();
        // code..
        return ResponseEntity.ok().body(entityModel);
    }
}
```

## 12 ) `Resource` Interface
```properties
# ℹ️ 로우 레벨 자원들에 접근을 추상화하기 위한 인터페이스
```  
### 12 - 1 ) 주요 메서드
- getInputStream() :
  - 자원을 탐색하여 열고 자원을 읽기 위해서 **InputStream 타입으로 반환**
- exists() : 
  - 접근하고자 하는 **자원 존재하는지 확인**
- isOpen() : 
  - **해당 자원에 접근**하고 있는 **스트림**이 있는지 **여부를 확인**
  - `true`일 경우 입력 스트림을 여러번 읽을 수 없음, 자원 누수를 방 지하기 위해 한번만 읽은 다음 닫아야 함
- getDescription() :
  - 자원을 사용할때 **오류 출력**에 사용되는 **설명을 반환**

### 12 - 2 ) Resource 구현 Class
- UrlResource
  - 파일 시스템, 웹 서버, FTP 서버, 클래스패스 등 다양한 위치의 리소스를 URL로 참조할 수 있다
    - 모든 URL들은 **표준화된 문자열 접두어**를 가짐
    - **접두어에 따른** Resource 인스턴스가 생성
    - URL 경로와 같이 API 메서드 호출시 묵시적으로 UrlResource 인스턴스 생성됨
      -  파일 경로 시 : `file:`   FileSystemResource 인스턴스로 생성
      -  HTTP 시 : `https:`  UrlResource 인스턴스가 생성
      -  FTP 시 : `ftp:`
- ClassPathResource
  - 프로젝트 내부의 파일을 읽어올 경우 사용
  -  쓰레드 컨텍스트 클래스 로더를 사용하거나  자원들을 불러오기 위해 사용
  - `src/main/resoruces/` 부터 **바로 접근 가능**
  - EX) 
  - `ClassPathResource resource = new ClassPathResource("data.json");`
- FileSystemResource
  -  **"java.io.File"** 클래스를 다루기 위한 클래스
- PathResource
  - `java.nio.file.Path`를 기반으로 파일 시스템의 리소스를 처리
  - 파일 경로를 독립적으로 다룰 수 있도록 하며, **File보다 더 유연하고 현대적인 파일 작업을 지원**
- ServletContextResource
  - 웹 애플리케이션의 루트 디렉토리 내부에서 상대적인 경로를 해석하는 ServletContext 자원들을 위해서 구현된 클래스
  - 루트 디렉토리를 기준으로 상대 경로를 해석하여 리소스를 처리
- InputStreamResource
  - 특정 리소스 구현이 적용되지 않는 경우에만 사용해야 합니다. 
    - 특히 ByteArrayResource나 파일 기반의 Resource 구현체가 해당 함
  - 반복 읽기가 필요한 작업에는 부적합
  - 주로 간단한 데이터 스트림 전달 작업에 사용
- ByteArrayResource
  - 클래스는 바이트 배열을 래핑하는 클래스
  - 파일 시스템을 사용하지 않고도 데이터를 다룰 수 있어, 임시 데이터 처리, 메모리 내 데이터 처리, 테스트 환경에서 유용

### 12 - 3 ) Header Content-Disposition 통한 다운로드
```properties
# ✅ HTTP Response Header에 들어가는 Content-Disposition은 HTTP Response Body에 오는 컨텐츠의 기질/성향을 알려주는 속성이다.
#    -  Content-Disposition에 attachment를 주는 경우로, 이때 filename과 함께 주게 되면 Body에 오는 값을 다운로드 받으라는 뜻이 된다.
#       ㄴ Ex) `Content-Disposition: attachment; filename="hello.jpg"`
```
#### Controller
```java
public class FileStorageController {
    private final FileStorageService fileStorageService;
    
    // 파일 다운로드
    @GetMapping("/download/model/{fileId}")
    public ResponseEntity<Resource> downloadModel(@PathVariable Long fileId) throws IOException {
        FileStorageDto fileStorageDto   = fileStorageService.getFileInfo(fileId);
        String contentDisposition ="attachment; filename=" + fileStorageDto.getOriginFileName() + "";
        String fullFilePath             = fileStorageService.fullFilePath(fileStorageDto);
        Resource resource               = new FileSystemResource(fullFilePath);
        String contentType              = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    // 이미지를 불러옴
    @GetMapping("/icon/{fileId}")
    public ResponseEntity<Resource> downloadIcon(@PathVariable Long fileId) throws IOException {
        FileStorageDto fileStorageDto   = fileStorageService.getFileInfo(fileId);
        String fullFilePath             = fileStorageService.fullFilePath(fileStorageDto);
        Resource resource               = new FileSystemResource(fullFilePath);
        String contentType              = Files.probeContentType(resource.getFile().toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}  
```
## 13 ) ServletUriComponentsBuilder
```properties
✅ HTTP 요청의 현재 컨텍스트를 기반으로 URI를 생성할 때 사용
```

### 주요 메서드
- `fromCurrentRequest()`: 현재 요청 정보를 기반으로 URI를 빌드합니다.
- `fromCurrentContextPath()`: 컨텍스트 경로를 기반으로 URI를 빌드합니다.
- `path(String path)`: URI 경로에 추가적인 경로를 설정합니다.
- `queryParam(String name, Object value)`: URI에 쿼리 파라미터를 추가합니다.
- `build()`: URI를 빌드합니다.
- `toUri()`: 최종 URI 객체를 반환합니다.

### 사용 예시
- fromCurrentRequest()
  - 현재 요청의 URI 정보를 기반으로 URI 빌드를 시작합니다.
- path("/{id}")
  - URI 경로에 추가적인 요소를 추가합니다.

- buildAndExpand(savedUser.getId())
  - 경로 변수({id})에 값을 동적으로 바인딩합니다.

- toUri()
  - 최종적으로 URI 객체를 생성합니다
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userService.save(user);

    // 생성된 리소스의 URI 생성
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();

    // Location 헤더와 함께 응답 반환
    return ResponseEntity.created(location).body(savedUser);
}
```
