<h1>REST API, RestTemplate, Swagger, GraphQL</h1>

<h3>1 ) REST API 란❔</h3>

- 👉 Representational State Transfer의 약자 이다. 
- - 자원(Data)을 이름으로 구분하여 해당 자원의 상태(정보)를 주고받는 것을 의미한다.
- - HTTP URI를 통해 자원을 명시하고, HTTP Method(POST, GET, PUT, DELETE)를<br/>
사용하여 해당 자원에 대한 CRUD를 적용하는것을 의미한다.
- 👉 각각의 Method 의미
- - POST : Create
- - GET  : Read
- - PUT  : Update
- - DELETE : Delete
- - HEAD : header 정보 조회
- 👉 REST의 장단점
- - 장점👍
- - - 1 . HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다.
- - - 2 . REST API 메세지가 의도하는 바를 명확하게 나타내므로 의도하는 바를 쉽게 파악 가능하다.
- - - 3 . 서버와 클라이언트의 역활을 명확하게 분리가 가능하다.
- - 단점👎
- - - 1 . 표준이 존재하지 않는다.
- - - 2 . 사용 가능한 메서드가 한정적이다.
- - - 3 . 불필요한 정보까지 가져올 수 있다. [GraphQL 과 비교됨]
- 👉 REST 구성요소
- - 1 . 자원(Resource) : URI
- - - 모든 자원에 고유한 ID가 존재하고, 이 자원은 Server에 존재한다.
- - - 모든 자원을 구별하는 ID는 "/user/{userNo}"와 같은 HTTP URI 형식이다.
- - - Client에서는 URI를 이용해서 자연월 지정하고 해당 자원의 상태에 대한 조작을 Server에 요청한다.
- - 2 . 행위(Verb) : HTTP Method
- - - HTTP 프로토콜의 Method를 사용한다.
- - - HTTP 프로토콜은 GET, POST, PUT, DELETE 와 같은 메서드를 제공한다.
- - 3 . 표현(Representation of Resource)
- - - Client가 자원의 상태에 대한 조작을 요청하면 Sever는 이에 적당한 응답(Response)울 보낸다.
- - - JSON, XML, TEXT, RSS 등 어려 형태의 응답으로 보내줄 수 있다. [ 일반적으로 JSON, XML로 응답함. ]
- 👉 REST 특징
- - 1 . Server-Client 구조이다.
- - - 자원이 있는 쪽은 Server , 자원을 요청하는 쪽은 Client 이다
- - - 위와 같은 구조로 서로간의 의존성을 줄인다.
- - 2 . Stateless(무상태) -- [ 💬 반대 개념 ? : Stateful(세션이 종료될 때까지의 통신 개념) ]
- - - HTTP Protocol은 Stateless Protocol이므로 REST 역시 무상태성을 갖는다.
- - - Client의 context를 Server에 저장하지 않는다.
- - - - 세션과 쿠키와 같은 context 정보를 신경쓰지 않아도 되므로 구현이 간단함.
- - - Server는 각각의 요청을 완전히 별개의 것으로 인식 처리
- - - - 간단하게 들어온 Method에 맞게만 처리해주면 된다.
- - - - 이전 요청이 다음 요청과 로직 및 처리가 연관 되어서는 안된다.
- - - - Server의 처리 방식에 일관성을 부여하고 부담을 줄여주며 서비스의 자유도가 향상된다.
- - 3 .  Cacheable(캐시 처리 기능)
- - - 웹 표준 HTTP 포로토콜을 그대로 사용하므로 웹에서 사용하는 기존 인프라를 그대로 사용 가능함.
- - 4 . Layered System(계층화)
- - - Client는 RESTAPI를 호출만 가능하다.
- - - REST Server는 다중 계층으로 구성될 수 있다.
- - - 로드 밸런싱, 공유 캐시 등을  통해 확장성과 보안성을 향상 시킬 수 있다.
- - 5 . Uniform Interface(인터페이스 일관성)
- - - URI로 지정한 Resource에 대한 조작을 통일되고 한정적인 인터페이스로 수행한다.
- - - HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다 [ 특정 언어나 기술에 종속 되지 ❌ ]
- 👉 REST API 설계 기본 규칙
- - 1 . URI는 정보의 자원을 표시 해야한다.
- - - resource는 동사 👎 - > 명사 👍
- - - resource는 대문자 👎 - > 소문자 👍
- - - 단수보다는 복수를 사용
- - - - Ex) GET /Member/1 👎  -> GET /members/1 👍  
- - 2 . 자원에 대한 행위는 HTTP Method로  표현한다.
- - - URI에 HTTP Method가 들어가면 안된다
- - - - Ex) GET /members/show/1 👎 -> GET /members/1
- - - - Ex) GET /members/insert/2 👎 -> POST /members/1
- - 3 . 슬래시 구분자(/)는 계층 관계를 나타내는데 사용한다.
- - - Ex) https://blackgom.com/cities/townships
- - 4 . URI 마지막 문자로 / 를 포함하지 않는다.
- - 5 . URI 경로에는 언더바( "_" ) 는  사용하지 ❌ // 불가피하게 URI가 긴 경우 하이픈( "-" )을 사용 👍
- - 6 . URI 경로에는 소문자가 적합하다.
- - - RFC 3986(URI 문법 형식)은 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정하기 때문
- - 7 . URI 경로에는 파일확장자를 포함하면 안된다. [ 💬 필요한다면 Accept Header에 추가해서 사용 ]
- - - Ex) https://blackgom.com/cities/townships/photo/336/yoo.jpg  👎
- - - Ex) https://blackgom.com/cities/townships/photo/336   ____ Accept:image/jpg  👍


<br/>
<hr/>

<h3>2 ) @RestController 란❔</h3>

- @RestController의 경우 모든 메서드의 리턴 타입은 기본으로 JSON을 사용한다.
- 해당 어노테이션을 사용하면 메서드 마다 @ResponseBody를 사용하지 않아도 된다.
- @RestController 와 @Controller 차이점 [ 간단설명 ]  
- - @Controller : Model 또는 @ResponseBody를 사용하여 데이터를 전달할 수 있지만 주된 기능은 View를 반환하기 위해 사용
- - @RestController : @Controller에 + @ResponseBody로 생각하면 된다, 주된 용도는 Json 형태로 객체 데이터를 반환하는 것이다.