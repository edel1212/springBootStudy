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
- - 장점
- - - 1 . HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다.
- - - 2 . REST API 메세지가 의도하는 바를 명확하게 나타내므로 의도하는 바를 쉽게 파악 가능하다.
- - - 3 . 서버와 클라이언트의 역활을 명확하게 분리가 가능하다.
- - 단점
- - - 1 . 표준이 존재하지 않는다.
- - - 2 . 사용 가능한 메서드가 한정적이다.
- - - 3 . 불필요한 정보까지 가져올 수 있다. [GraphQL 과 비교됨]