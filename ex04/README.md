<h1>REST API, RestTemplate, Swagger, GraphQL</h1>

<h3>1 ) REST API 란❔</h3>

- 👉 Representational State Transfer의 약자 이다. 
  - 자원(Data)을 이름으로 구분하여 해당 자원의 상태(정보)를 주고받는 것을 의미한다.
  - HTTP URI를 통해 자원을 명시하고, HTTP Method(POST, GET, PUT, DELETE)를<br/>
사용하여 해당 자원에 대한 CRUD를 적용하는것을 의미한다.
- 👉 각각의 Method 의미
  - POST : Create
  - GET  : Read
  - PUT  : Update
  - DELETE : Delete
  - HEAD : header 정보 조회
- 👉 REST의 장단점
  - 장점👍
    - 1 . HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다.
    - 2 . REST API 메세지가 의도하는 바를 명확하게 나타내므로 의도하는 바를 쉽게 파악 가능하다.
    - 3 . 서버와 클라이언트의 역활을 명확하게 분리가 가능하다.
  - 단점👎
    - 1 . 표준이 존재하지 않는다.
    - 2 . 사용 가능한 메서드가 한정적이다.
    - 3 . 불필요한 정보까지 가져올 수 있다. [GraphQL 과 비교됨]
- 👉 REST 구성요소
  - 1 . 자원(Resource) : URI
    - 모든 자원에 고유한 ID가 존재하고, 이 자원은 Server에 존재한다.
    - 모든 자원을 구별하는 ID는 "/user/{userNo}"와 같은 HTTP URI 형식이다.
    - Client에서는 URI를 이용해서 자연월 지정하고 해당 자원의 상태에 대한 조작을 Server에 요청한다.
  - 2 . 행위(Verb) : HTTP Method
    - HTTP 프로토콜의 Method를 사용한다.
    - HTTP 프로토콜은 GET, POST, PUT, DELETE 와 같은 메서드를 제공한다. 
  - 3 . 표현(Representation of Resource)
    - Client가 자원의 상태에 대한 조작을 요청하면 Sever는 이에 적당한 응답(Response)울 보낸다.
    - JSON, XML, TEXT, RSS 등 어려 형태의 응답으로 보내줄 수 있다. [ 일반적으로 JSON, XML로 응답함. ]
- 👉 REST 특징
  - 1 . Server-Client 구조이다.
    - 자원이 있는 쪽은 Server , 자원을 요청하는 쪽은 Client 이다
    - 위와 같은 구조로 서로간의 의존성을 줄인다.
  - 2 . Stateless(무상태) -- [ 💬 반대 개념 ? : Stateful(세션이 종료될 때까지의 통신 개념) ]
    - HTTP Protocol은 Stateless Protocol이므로 REST 역시 무상태성을 갖는다.
    - Client의 context를 Server에 저장하지 않는다.
      - 세션과 쿠키와 같은 context 정보를 신경쓰지 않아도 되므로 구현이 간단함.
    - Server는 각각의 요청을 완전히 별개의 것으로 인식 처리
      - 간단하게 들어온 Method에 맞게만 처리해주면 된다.
      - 이전 요청이 다음 요청과 로직 및 처리가 연관 되어서는 안된다.
      - Server의 처리 방식에 일관성을 부여하고 부담을 줄여주며 서비스의 자유도가 향상된다.
  - 3 .  Cacheable(캐시 처리 기능)
    - 웹 표준 HTTP 포로토콜을 그대로 사용하므로 웹에서 사용하는 기존 인프라를 그대로 사용 가능함.
  - 4 . Layered System(계층화)
    - Client는 RESTAPI를 호출만 가능하다.
    - REST Server는 다중 계층으로 구성될 수 있다.
    - 로드 밸런싱, 공유 캐시 등을  통해 확장성과 보안성을 향상 시킬 수 있다.
  - 5 . Uniform Interface(인터페이스 일관성)
    - URI로 지정한 Resource에 대한 조작을 통일되고 한정적인 인터페이스로 수행한다.
    - HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다 [ 특정 언어나 기술에 종속 되지 ❌ ]
- 👉 REST API 설계 기본 규칙
  - 1 . URI는 정보의 자원을 표시 해야한다.
    - resource는 동사 👎 - > 명사 👍
    - resource는 대문자 👎 - > 소문자 👍
    - 단수보다는 복수를 사용
      - Ex) GET /Member/1 👎  -> GET /members/1 👍  
  - 2 . 자원에 대한 행위는 HTTP Method로  표현한다.
    - URI에 HTTP Method가 들어가면 안된다
      - Ex) GET /members/show/1 👎 -> GET /members/1
      - Ex) GET /members/insert/2 👎 -> POST /members/1
  - 3 . 슬래시 구분자(/)는 계층 관계를 나타내는데 사용한다.
    - Ex) https://blackgom.com/cities/townships
  - 4 . URI 마지막 문자로 / 를 포함하지 않는다.
  - 5 . URI 경로에는 언더바( "_" ) 는  사용하지 ❌ // 불가피하게 URI가 긴 경우 하이픈( "-" )을 사용 👍
  - 6 . URI 경로에는 소문자가 적합하다.
    - RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정하기 때문
  - 7 . URI 경로에는 파일확장자를 포함하면 안된다. [ 💬 필요한다면 Accept Header에 추가해서 사용 ]
    - Ex) https://blackgom.com/cities/townships/photo/336/yoo.jpg  👎
    - Ex) https://blackgom.com/cities/townships/photo/336   ____ Accept:image/jpg  👍


<br/>
<hr/>

<h3>2 ) @RestController 란❔</h3>

- @RestController의 경우 모든 메서드의 리턴 타입은 기본으로 JSON을 사용한다.
- 해당 어노테이션을 사용하면 메서드 마다 @ResponseBody를 사용하지 않아도 된다.
- @RestController 와 @Controller 차이점 [ 간단설명 ]  
  - @Controller : Model 또는 @ResponseBody를 사용하여 데이터를 전달할 수 있지만 주된 기능은 View를 반환하기 위해 사용
  - @RestController : @Controller에 + @ResponseBody로 생각하면 된다, 주된 용도는 Json 형태로 객체 데이터를 반환하는 것이다.

\- Controller - GET 방식🔽
```java
// java - Controller

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/replies/")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
    }


}
```

\- View [ javascript ] - GET 방식🔽
```javascript
// javascript - use fetchAPI

fetch("/replies/board/90") // bno : 90번을 찾음
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));

```

<br/>
<hr/>

<h3>3 ) @RequestMapping의 Consumes 와  Produces 란❔</h3>

- Consumes : 소비 가능한 미디어 타입을 지정하는 것이며 주요한 매핑을 제한 할수있다.
  - HTTP 통신 대상의 Content-Type 요청 헤더가 Consumes에 지정한 미디어 타입과 일치할 때만 요청이 성공한다.
  - Get 방식일 경우에는 Consumes가 불필요하다 [ Get방식의 데이터 전달 방식은 URI형태로 받기에 Body가 없기 때문이다. ]
  - 💬 간단설명 : <strong>consumes는 클라이언트가 서버에게 보내는 데이터 타입을 명시한다.</strong>

\- Consumes Test Controller [ ☠️ Error Case ]🔽
```java
//java - Controller

@Description("Error Case Get방식은 Body가 없으므로 consumes가 불필요함")
@Deprecated
@GetMapping(value = "/consumesErrorCase1", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<String> errorCase1(@RequestBody Map<String, String> testValue){

        log.info("testValue :: {}", testValue);
        
        return ResponseEntity.ok().body("ErrorCase");
}
```

\- Consumes Test Client [ ☠️ Error Case ]🔽
```javascript
//javascript - Client

/*
    GetMethod 방식에는 Body가 들어갈수가 없음!!
    1 ) 헤더의 내용중 BODY 데이터를 설명하는 Content-Type이라는 헤더필드는 들어가지 않는다.
    2 ) TypeError: Failed to execute 'fetch' on 'Window': Request with GET/HEAD method cannot have body.
 */
function errorCase(){
    fetch("/replies/consumesErrorCase1"
        ,{
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                testValue: 123
                })
          })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
}
```

\- Consumes Test Controller [ 👍 Success Case ]🔽
```java
//java - Controller

@PostMapping(value = "/consumesSuccess", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, String>> errorCase2(@RequestBody Map<String, String> testValue){

        log.info("testValue :: {}", testValue);

        Map<String , String > result = new HashMap<>();
        result.put("result","SUCCESS");

        return ResponseEntity.ok().body(result);
}
```
\- Consumes Test Client [ 👍 Success Case ]🔽
```javascript
function consumesSuccess(){
    fetch("/replies/consumesSuccess"
        ,{
            method: "POST" ,
            headers: {
                "Content-Type": "application/json", // consumes 와 맞춰줘야한다!
            },
            body: JSON.stringify({
                testValue: 123
                })
          })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
}
```

- Produces : Server단에서 보내주는 데이터 타입을 정의한다.
  - client에서 받는 데이터 형식을 정하는 Header는 Accept이다.
  - 💬 간단설명 : <strong>produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시한다</strong>

\- Produces Test Controller [ ☠️ Error Case ]🔽
```java
//java - Controller

/*
 * 반환 타입과 produces 설정 또한 맞지 않음
 * */
@Description("반환 타입과 produces가 맞지 않기에 500Error 반환")
@Deprecated
@GetMapping(value = "/errorCase/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
public ResponseEntity<List<ReplyDTO>> producesErrorCase(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
}

/*
 * Server단에서는 문제가 없지만 Cleint단 에서  모순되는 문제가 있음
 * */
@Description("Error는 없지만 Client단에서의 모순이 있음")
@Deprecated
@GetMapping(value = "/errorCase2/{bno}", produces = MediaType.TEXT_PLAIN_VALUE)
public ResponseEntity<String> producesErrorCase2(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body("Yoo");
}
```

\- Produces Test Client [ ☠️ Error Case ]🔽
```javascript
//javascript - Cleint

/**
 이유 : Server에서 반환 타입은 [{}]형식의 JSON 형식이지만
       produces = MediaType.TEXT_PLAIN_VALUE 로 설정하였기에
       에러를 반환함
 Error Code :500
*/
function  producesErrorCase (){
    fetch("/replies/errorCase/90")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
}

/**
 이유 : 해당 테스트는 에러는 없지만 Client 의 Accept 와 Server단의 produces, return 타입이 다른
      문제가 있고 사실상 해당 fetchAPI 사용에서도 모순되는 점이 있다
      - header -> Accept 를 json으로 설정했으면서도
      - 받아오는 타입의 데이터는  response.text()를 사용 [ String을 반환하기 때문 ]
        한다. .json()은 Error가 나기 때문이다.
 Error Code : 없음
*/
function  producesErrorCase2 (){
    fetch("/replies/errorCase2/90"
        ,{
            method : "GET" ,
            header : {Accept : "application/json"}
        })
      .then((response) => response.text())
      //.then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
}
```

\- Produces Test Controller [ 👍 Success Case ]🔽
```java
//java - Controller

@GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable Long bno){
        log.info("bno ::: {}" , bno);
        return ResponseEntity.ok().body(replyService.getList(bno));
}
```
\- Produces Test Client [ 👍 Success Case ]🔽
```javascript
//javascript - Client

//성공
function getReplies(){
    fetch("/replies/board/90")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => console.log(error));
}
```

<br/>
<hr/>

<h3>5 ) Content-Type의 application/json 과 application/x-www-from-urlencoded 차이점❔</h3>

- 대부분의 HTTP Request에 대한 Content-Type은 application/json이 대부분이다.[ REST API 대중화 때문 ]
<br/> <strong>단 !!!</strong> application/x-www-form-urlencoded는 html form의 기본 전송 시 Content-Type 이므로
자주 사용되지는 않지만 가끔씩 사용된다.
- 차이점 ?
  - application/json : {key: value}의 JSON형태로  Server에 전송된다.
  - application/x-www-form-urlencoded : key=value&key=value의 형태로 전달된다는 점입니다.
- 👉 applcation/x-www-form-urlencoded 사용 시 주의점
  -  application logic에서 applcation/x-www-form-urlencoded를 사용할 경우 body 인코딩이 
<br/>해당 framework 혹은 library에서 자동으로 되는지 확인 후 안되면 해줘야한다.
<br/> Ex) body : stringify(form).toString('utf8')
\- Test Code [ Client ]🔽
```html
<!-- html -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    
    <!--  
      http://localhost:8081/replies/formVer?testValue=95 
      위와 같이 값이 전달된다.
      -->
    <form action="/replies/formVer">
        <input name="testValue" value="95">
        <button>전송</button>
    </form>

    <hr/>
    
    <!--  Server단 파라미터 타입 다르게 테스트   -->
    <form action="/replies/formVer2">
        <input name="bno" value="95">
        <button>전송</button>
    </form>

    <hr/>
    
    <!--  Post 방식  -->
    <form action="/replies/formAndPostVer" method="post">
        <input name="bno" value="95">
        <button>전송</button>
    </form>

</body>
</html>
```

\- Test Code [ Server ]🔽
```java
//java - Controller

@Description("URL에 값이 담겨나옴")
@GetMapping(value = "/formVer")
public ResponseEntity<List<ReplyDTO>> applicationFormVerTest(Long testValue){
        log.info("bno ::: {}" , testValue);
        return ResponseEntity.ok().body(replyService.getList(testValue));
}

@Description("DTO에 값이 담기는지 확인")
@GetMapping(value = "/formVer2")
public ResponseEntity<List<ReplyDTO>> applicationFormVerTest(ReplyDTO replyDTO){
        log.info("bno ::: {}" , replyDTO);
        return ResponseEntity.ok().body(replyService.getList(replyDTO.getBno()));
}

/**
 * Parameter를 (Long testValue) 받았을 시 이상없음 확인 완료
 * */
@PostMapping(value = "/formAndPostVer")
public ResponseEntity<List<ReplyDTO>> applicationFormAndPostVerTest(ReplyDTO replyDTO){
        log.info("bno ::: {}" , replyDTO);
        return ResponseEntity.ok().body(replyService.getList(replyDTO.getBno()));
}
```

<br/>
<hr/>

<h3>6 ) Swagger Setting </h3>

- 1. build.gradle에 Swagger dependencies 추가
```properties
# build.gradle

code...

dependencies {
        code...

        //Swagger 추가
        // https://mvnrepository.com/artifact/io.springfox/springfox-swagger2
        implementation 'io.springfox:springfox-boot-starter:3.0.0'
        // UI 를 추가 안할시 404 WithPage가 나옴
        implementation 'io.springfox:springfox-swagger-ui:3.0.0'

}

code...

```

- 2. application.properties 설정 추가
<br/> 💬 Spring boot 2.6버전 이후에 spring.mvc.pathmatch.matching-strategy 값이 
<br/>ant_apth_matcher에서 path_pattern_parser로 변경되면서 몇몇 라이브러리에서 오류가 발생
```properties
#application.properties

#Swagger Setting 
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
```


- 3. 추가한 Swagger Config 설정 class 추가 및 설정
```java
//java - src -> main -> projectDir -> config -> 설정 class
@Configuration  // scan 대상에 추가
public class SwaggerConfiguration {

    private static final String API_NAME = "Programmers Spring Boot Application - yoo";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Swagger!";

    @Bean // Bean 등록
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())  // 설정정보를 Parameter로 추가[ ApiInfo Type ]
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yoo.ex04")) // Swagger API를 생성할 BasePackage 범위 지정
                .paths(PathSelectors.any()) // apis 에 위치하는 API 중 특정 path 를 선택
                .build();
    }

    /***
     * @Description : Swagger Setting info
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Rest API Swagger - yoo")
                .description("Swagger!!!")
                .version("1.0")
                .build();
    }

}
```

- 4. 사용 URL : http://localhost:9999/swagger-ui/#/ - port는 자신의 prot에 맞춰주자!

<br/>
<hr/>

<h3>7) RestTemplate 이란 ?</h3>
- Spring에서 제공하는 HTTP통신 기능을 쉽게 사용할 수 있게 설계 되어 있는 템플릿이다.
- Spring-Boot-Start에는 자동 내장읻 되어있기에 따로 설정이 필요없다.
- HTTP 서버와의 통신을 단순화하고 RESTful 원칙을 지킴
- 동기 방식으로 처리되며, 비동기 방식으로는 ( AsyncRstTemplate )가 있다.
- 대표적으로 사용되는 RestTemplate의 메서드 🔽
  - Server단에서 다른 서버에 요청하여 데이터를 처리할때 사용됨 [ client단에서의 요청이 아님! ]
- ☠️☠️☠️ RestTemplate 는 Deprecated 될 예정이라는 글을 확인했다... 
<br/> 👍 대신에 WebClient를 사용한다 하는데 해당 부분은 WebClient를 설명할때 작성 ！ 

<table style="width:100%">
    <tableHead>
        <td>Method</td>
        <td>HTTP</td>
        <td>설명</td>
    </tableHead>
    <tr>
        <td>getForObject</td>
        <td>GET</td>
        <td>GET형식으로 요청하여 갹체로 결과를 반환 받음</td>
    </tr>
    <tr>
        <td>getForEntity</td>
        <td>GET</td>
        <td>GET형식으로 요청하여 ResponseEntity로 결과를 반환 받음</td>
    </tr>
    <tr>
        <td>postForObject</td>
        <td>POST</td>
        <td>POST형식으로 요청하여 갹체로 결과를 반환 받음</td>
    </tr>
    <tr>
        <td>postForEntity</td>
        <td>POST</td>
        <td>POST형식으로 요청하여 ResponseEntity로 결과를 반환 받음</td>
    </tr>
    <tr>
        <td>delete</td>
        <td>DELETE</td>
        <td>DELETE형식으로 요청</td>
    </tr>
    <tr>
        <td>put</td>
        <td>PUT</td>
        <td>PUT형식으로 요청</td>
    </tr>
    <tr>
        <td>patchForObject</td>
        <td>PATCH</td>
        <td>PATCH형식으로 요청</td>
    </tr>
    <tr>
        <td>exchange</td>
        <td>any</td>
        <td>HTTP 해더를 생성하여 추가할 수 있고 어떤 형식의 Method 방식에서도 사용할 수 있음</td>
    </tr>
</table>

\- RestTemplate GET방식 [ 요청 Server ]🔽

- 💬 간단설명 : getForEntity, getForObject  차이점 ?
  - getForEntity : 반환타입을 ResponseEntity로 받기에 ResponseEntity 객체에 
<br/> responseEntity.getBody() 메서드를 사용해서 데이터를 끄내줘야한다.
  - getForObject : 반환타입을 Object로 받기에 따로 한번 더 데이터를 추출해줄 필요가 없다.


<br/>

- <strong>Parameter가 없는</strong> 방식 ↓
```java
// java - RestTemplate - Parameter ❌

// RestTemplate 요청 ServiceImpl
@Service
@Log4j2
public class RestTemplateServiceImpl implements RestTemplateService {

  private final String TARGET_URI = "http://localhost:8080";

  /**
   * @Desription : 요청 시 parameter 를 사용하지 않는 형식
   * */
  @Override
  public String getHelloWorld() {

    URI uri = UriComponentsBuilder
            .fromUriString(TARGET_URI) // 1. URI를 넣어준다
            .path("/replies/restTest") // 2. URL Path를 추가
            .encode()                  // 3. encode 해준다
            .build()                   // 4. build
            .toUri();                  // 5. 만들어진 객체를 URI로 변환
    
    // 6. RestTempate 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    // 7. RestTemplate의 getForEntity()를 사용 했으므로 반환 타입은
    //    ResponseEntity로 맞춰준다.
    //    - Parameter로는( URI , 해당 요청의 반환 타입으로 맞춰준다 )
    //    - 💬 여기서 문제가 발생하는데 응답해주는 서버에서 Collection을 보내든
    //         int 타입을 보내든 전부 String으로 받을수 있다
    //         받은 후 파싱 처리가 핋요함.
    //    - 해당 케이스들은 아래 예제에서 확인하자
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

    log.info("status code :: {}", responseEntity.getStatusCode());
    log.info("body :: {}", responseEntity.getBody());

    return responseEntity.getBody();
  }
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@Log4j2
@RestController
@RequestMapping("/replies/")
@RequiredArgsConstructor
public class ReplyController {
  /**
   * 알게된 새로운 사실 파리미터인 name을 받을 때
   * @ReqeustParam을 사용할시 해당 name 파라미터가 강제됨 값이 없을 경우 에러를 반환함.
   * -> 기본 설정이 필수값으로 설정 되어 있기  때문임.
   *
   * ex) (@RequestParam String name)
   *     -> http://localhost:8080/replies/restTest?name=yoo  :: 이상없음
   *     -> http://localhost:8080/replies/restTest           :: 에러 발생☠️
   *      Error Code : 400 [bad Request]
   *      Error Msg  : Required request parameter 'name' for method
   *                   parameter type String is not present
   * 해결방안 : 1) required = boolean  옵션을 사용하여 필수 요소를 지정이 가능하다.
   *             - (@RequestParam(required = false) String name )
   *         2) defaultValue = "??"  지정이 가능하다.
   *             - (@RequestParam(required = false
   *                            , defaultValue = "흑곰" ) String name )
   * */
  @GetMapping("/restTest")
  public String getStringWithRestTempTest(@RequestParam(required = false) String name ){

    log.info("name ::: {} ", name);

    String result =
            name != null ? "Hello " + name + " World" : "Hello World";

    return result;
  }    
}
```

<br/>

- <strong>Parameter가 있는</strong> 방식 ↓
  - .queryParam(key, value) 를 사용하여 값을 전달함
```java
// java - RestTemplate - Parameter 🆗

// RestTemplate 요청 ServiceImpl

/**
 * @Desription : 요청 시 parameter 를 사용한 형식
 * */
@Override
public String getHelloWordWithParam() {
        URI uri = UriComponentsBuilder
        .fromUriString(TARGET_URI)
        .path("/replies/restTest")
        .queryParam("name","yoo!!")  // 💬 파라미터가 추가되었음
        .encode()
        .build()
        .toUri();

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("status code :: {}", responseEntity.getStatusCode());
        log.info("body :: {}", responseEntity.getBody());

        return responseEntity.getBody();
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@GetMapping("/restTest")
public String getStringWithRestTempTest(@RequestParam(required = false) String name ){

        log.info("name ::: {} ", name);

        String result =
        name != null ? "Hello " + name + " World" : "Hello World";

        return result;
}
```

<br/>

- <strong>PathVariable</strong>을 사용한 방식 ↓
  - .path("/replies/board/{bno}") &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// URL Mapping 방식처럼 {??}를 사용하여 값을 받을 곳을 지정함
  - .expand(90L)                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// 지정된 곳에 값을 추가한다 현재는 bno : 90 번을 조회
```java
// java - RestTemplate - PathVariable 

// RestTemplate 요청 ServiceImpl
/**
 * @Desription : 요청 시 PathVariable을 사용한 방식
 * */
@Override
public String getListByBoardVerStr() {
        URI uri = UriComponentsBuilder
        .fromUriString(TARGET_URI)
        .path("/replies/board/{bno}") // URL Mapping 방식처럼 {??}를 사용하여 값을 받을 곳을 지정함
        .encode()
        .build()
        .expand(90L)                 // 지정된 곳에 값을 추가한다 현재는 bno : 90 번을 조회
        .toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        log.info("status code :: {}", responseEntity.getStatusCode());

        log.info("body :: {}", responseEntity.getBody());

        return responseEntity.getBody();
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@GetMapping(value = "/board/{bno}" // PathVariable 사용
        , produces = MediaType.APPLICATION_JSON_VALUE) //반환 타입은 JSON 명시
public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno")Long bno){
        log.info("bno :: " + bno);
        return new ResponseEntity<>(replyService.getList(bno),HttpStatus.OK);
}

```

<br/>

- <strong>Data를 배열로 받아와서 사용</strong>을 사용한 방식 ↓
  - restTemplate.getForEntity(uri, ReplyDTO[].class );    &nbsp;&nbsp;&nbsp;&nbsp;   // 1 . 해당 ResponeEntity< DTO[] > 배열형식으로 받는다.
  - List<ReplyDTO> lst = List.of(responseEntity.getBody()); &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;//  2. 지정된 곳에 값을 추가한다 현재는 bno : 90 번을 조회
```java
// java - RestTemplate - body값을 배열로 받아 사용

// RestTemplate 요청 ServiceImpl
@Override
public List<ReplyDTO> getListByBoardVerLst() {
    URI uri = UriComponentsBuilder
            .fromUriString(TARGET_URI)
            .path("/replies/board/{bno}")
            .encode()
            .build()
            .expand(90L)
            .toUri();
    RestTemplate restTemplate = new RestTemplate();
    //1 . 해당 ResponeEntity< 형식[] > 배열형식으로 받아
    //    - <List<ReplyDTO>> 형식으로는 값을 받아오는것이 불가능하다!! 그렇기에 DTO형식을 배열로 받은 후 처리함.    
    ResponseEntity<ReplyDTO[]> responseEntity = restTemplate.getForEntity(uri, ReplyDTO[].class );
    
    //2 . List<>로 파싱하여 사용이 가능하다. 
    List<ReplyDTO> lst = List.of(responseEntity.getBody());

    log.info("status code :: {}", responseEntity.getStatusCode());

    log.info("body :: {}", lst);

    return lst;
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@GetMapping(value = "/board/{bno}" // PathVariable 사용
        , produces = MediaType.APPLICATION_JSON_VALUE) //반환 타입은 JSON 명시
public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno")Long bno){
        log.info("bno :: " + bno);
        return new ResponseEntity<>(replyService.getList(bno),HttpStatus.OK);
}
```
<br/>

- <strong>restTemplate.getForObject</strong>을 사용하여 데이터를 받아오는 방식 ↓
  - restTemplate.getForEntity() 와 차이점은 간단하게 설명해서 그냥 상단에서 설명한대로 반환하는 형식의 차이일뿐이다
    - restTemplate.getForEntity() : ResponseEntity 타입이므로 .getBody()를 사용하여 데이터를 추출해야한다.
      - Header 값등을 가져올수 있어서 해당 방식이 좀 더 안정적인 방식임 
    - restTemplate.getForObject() : 바로 해당 타입에 접근하여 값을 가져올 수 있다.
      - ex)  ReplyDTO[] result = restTemplate.getForObject(uri, ReplyDTO[].class ); &nbsp;&nbsp; // 반환 타입이 바로 ReplyDTO[] 이다 !
```java
// java - RestTemplate - getForObject()를 사용해서 값을 가져옴

// RestTemplate 요청 ServiceImpl
@Override
public List<ReplyDTO> getListByBoardVerLstAndObject() {
        URI uri = UriComponentsBuilder
        .fromUriString(TARGET_URI)
        .path("/replies/board/{bno}")
        .encode()
        .build()
        .expand(90L)
        .toUri();
        RestTemplate restTemplate = new RestTemplate();

        // getForEntity()와 는 다르게 반환 타입이 ReplyDTO[] 임을 확인 할수 있다.
        ReplyDTO[] result = restTemplate.getForObject(uri, ReplyDTO[].class );

        List<ReplyDTO> lst = List.of(result);

        return lst;
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@GetMapping(value = "/board/{bno}" // PathVariable 사용
        , produces = MediaType.APPLICATION_JSON_VALUE) //반환 타입은 JSON 명시
public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno")Long bno){
        log.info("bno :: " + bno);
        return new ResponseEntity<>(replyService.getList(bno),HttpStatus.OK);
}
```  

<br/>

- <strong>POST 방식</strong>을 사용하여 데이터를 Insert ↓
  - restTemplate.postForEntity(uri,replyDTO, Long.class); // (URI , 저장할 데이터, 반환값)형식으로 POST로 보내주면 된다.
  - ✅ 주의사항으로 : 요청을 응답해주는 서버쪽 URL을 잘확인하자 ../URL/ 형식일수도있음 .. POST 이기에 URL의 뒷부분이 없을수도 있기 떄문!! 
```java
// java - RestTemplate - POST 요청

// RestTemplate 요청 ServiceImpl
/**
 * POST 방식 [Insert]
 * */
@Override
public ResponseEntity<Long> replyRegister() {

    // 1. URI 객체 생성
    URI uri = UriComponentsBuilder
            .fromUriString(TARGET_URI)
            .path("/replies/") // 여기서 /URL 로 해서 404 떨어짐 /URL/로 해야함 받는 쪽 RequestMapping확인 하자!!  
            .encode()
            .build()
            .toUri();

    // 2. RestTemplate 객체 생성
    RestTemplate restTemplate = new RestTemplate();

    // 3 . Insert 를 위한 ReplyDTO 객체 생성
    //     - 해당 로직은 테스트 로직이기에 데이터를 받아오지 않고
    //       로직에서 Dummy data 생성
    ReplyDTO replyDTO = ReplyDTO.builder()
            .text("RestTemplateTest")
            .bno(101L)
            .replyer("guest1")
            .build();

    // 4 . postForEntity(URI , Parameter , 반환타입) 사용하여 POST 요청
    ResponseEntity<Long> result = restTemplate.postForEntity(uri,replyDTO, Long.class);

    return result;
}


////////////////////////////////////////////////////////////////////////////


// RestTemplate 응답 Controller
@PostMapping
public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        Long newRno = replyService.register(replyDTO);
        return new ResponseEntity<>(newRno,HttpStatus.OK);
}
```
//TODO : DELETE Type Test Code