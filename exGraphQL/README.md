<h1>GraphQL</h1>

<h3>1 ) GraphQL 이란❔</h3>

- Facebook에서 개발한 API를 위한 쿼리 언어이자 서버측 런타임으로 클라이언트에게 요청한 만큼의 데이터를 제공한다.
- REST API를 대체할수 있는 쿼리 언어이다.
- 서버와 클라이언트 사이에 효율적으로 데이터를 주고 받는 것이 목적

<br/>
<hr/>
<h3>2 ) Rest API 와 GraphQL의 차이</h3>

- Rest API는 url에 HTTP Method(GET, POST, PUT, DELEETE)를 사용하여 데이터를 주고받는 방식
  - Rest API 경우 
    - 다양한 엔트포인트가 존재한다 ( /member , /board, /board/reply ... 등등 )
    - Under-Fetching / Over-Fetching 문제가 있다.
      - Under-Fetching : 요청했단 데이터에 원하는 데이터가 부족하여 필요한 요청을 다시 해야함.
      - Over-Fetching : 요청했던 데이터에 불필요한 데이타까지 포함되어 넘어와 자원낭비가 된다.
  - GraphQL 경우
    - 보통의 경우에 /graphql이라는 엔트포인트만 존재한다.

<br/>
<hr/>
<h3>3 ) GraphQL의 장단점</h3>

- 장점
  - Client에서 필요한 최소한의 데이터만 요청이 가능하기에 Http요청이 줄어들어 효율적임
  - 엄격하게 정의돈 데이터 유형은 Client 와 Server간의 통신 오류를 줄일 수 있음
  - 기존 쿼리르 중단하지 않고 진화할수 있도록 허용한다 (무중단 배포)
  - Rest API의 한계인 Over-Fetching 과 Under-Fetching을 개선해줌.
- 단점
  - File 전송 등 Text로만 하기 힘든 작업을 처리하기 복잡함
    - 고정된 요청과 응다만 필요할 경우 Query로 인해 요청의 크기가 Rest API 보다 더 커진다


<br/>
<hr/>
<h3>4 ) Spring For GraphQL 설정 및 사용 방법</h3>

- 💬 이전에는 graphql-java / graphql-java-spring, graphql-java-kickstart / graphql-spring-boot 
<br/> ,  Netflix / dgs-framework 3개의 대표적인 라이브러리/프레임워크 중 한가지를 선택해서 Spring에서 GraphQL을 구현하였으나
<br/> 앞에서 설명한 라이브러리/프레임워크는 Resolver를 개발하고 별도의 설정이 필요하였으나
<br/> 👉 Spring for GraphQL은 Spring에서 공식 릴리즈하여 별도의 설정없이 Spring에서 추구하는 추가적인 코드드없이
<br/> 기존 MVC를 개발하듯 개발이 가능해졌다.👍
- 👉 Spring for GraphQL은 SpringBoot 2.7.0 버전 이상부터 지원합니다.

<br/>

- 1 . build.gradle에 dependencies 추가
  - Spring Initializr 에서 바로 추가 가능 
```build.gradle
//build.gradle

..code...

dependencies {
    ..code...
	implementation 'org.springframework.boot:spring-boot-starter-graphql'
	testImplementation 'org.springframework.graphql:spring-graphql-test'
	..code...
}

..code...
 
```

<br/>

- 2 . application.properties 설정
```properties
#application.properties

...code...

#################
#GraphQL Setting#
#################
### Endpoint  URI ###
graphql.servlet.mapping=/graphql
### Mapping dir ###
graphql.tools.schema-location-pattern=**/*.graphqls
graphql.servlet.cors-enabled=true
graphql.servlet.max-query-depth=100
graphql.servlet.exception-handlers-enabled=true

```

<br/>

- 2 . *.graphqls 파일 설정
  - 기본적으로 Directory Paht는 resources/graphql/*.graphqls 이다.
  - GraphQL 데이터 요청 시 2가지 타입이 존재한다 ( Query Type, Mutation Type)
    - Query Type : Read 종류는 전부 Query 타입이다 [ 단건 , 다건 모두 ]
    - Mutation Type : Insert, Update, DELETE 는 전부 Mutation Type 이다.
  - 👉 <strong>[ 중요 ]</strong> 요청하려는 Query, Mutation에서 정의한 메서드명과 Controller단의 메서드명이 일치해야한다.
  - ☠️ Type 설정중 파라미터를 받는 부분이 있는데 ex :: (memberEntity : MemberEntity)와 같이 <strong>TypeScript 형식으로 받아</strong>야하는데
  <br/> (MemberEntity : memberEntity )와 같이 Java 형식으로 받으면 서버 기동 시 에러남 ...
  <br/> ☠️☠️이걸로 삽질했었음 .. 잊지말자..☠️☠️
  - 각 필드는 Int, Float, String Boolean, ID 같은 기본 타입(스칼라 타입)으로 표현할 수 있다.
  - 콜론 ' : '을 기준으로 왼쪽은 컬럼명 오른쪽은 스칼라 타입을 의미한다
  - 스칼라 타입 뒤에 붙은 ' ! '는 null 값을 허용하지 않는다는 non-nullable의 뜻을 가지고 있다.
```graphqls
#*.graphqls File

schema {
    query: Query,
    mutation: Mutation,
}

# 요청 타입 및 요청 메서드명
type Mutation {
    registerMember(memberEntity : MemberEntity): String
    updateMember(memberEntity : MemberEntity): Boolean
    deleteMember(email: String!): Boolean
}

# 요청 타입 및 요청 메서드명
type Query{
	findMember(email: String!): Member
	allFindMembers: [Member]
}


# 응답 : member
type Member{
	email: String
	password: String
	name : String
	regDate : String
	modDate : String
}

# parameter 구조를 만들줌
input MemberEntity{
	email: String
	password: String
	name : String
	regDate : String
   	modDate : String
}
```


<br/>

- 4 . Business Logic [ Service -> Repository 로직은 기존과 크게 다를게 없기에 Read, Insert를 제외한 나머지 로직은 생략 ] 
  
```java
// Service 
public interface MemberService {
    //Query Type
    MemberDTO findMember(String email);
    
    ... code ...
    
    
    ///////////////////////////////


    //Mutation Type
    String registerMember(MemberDTO member);   
    
    ... code ...
}


////////////////////////////////////////////////////////////////////

// ServiceImpl
@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    
  private final MemberRepository memberRepository;

  ... code ...
  
  @Override
  public MemberDTO findMember(String email) {
    log.info("findMember ::: {}", email);
    return this.entityToDto(memberRepository.findById(email).get());
  }
  
  ///////////////////////////////////////////////////////

  @Override
  public String registerMember(MemberDTO member) {
    log.info("registerMember :: {}", member);
    Member result = memberRepository.save(this.dtoToEntity(member));
    return member.getEmail();
  }  
  
  ... code ...
}

```

<br/>

- 5 . Controllers 
  - 👉 Query Type 경우에는 @QueryMapping를 사용해 줘야한다.
  - 👉 Mutation Type 경우에는 @MutationMapping 사용해 줘야한다
  - 💬 *.grpahqls 파일의 메서드명과 Controller에서 Mapping하는 메서드명은 꼭 같아야한다!!
  - 👉  파라미터를 받을때는 @Argument를 사용해서 받아줘야 한다.
  - 👉 Client에서 보내는 파라미터와 Server에서 이름이 다를 경우 @Argument("???")를 사용해서 받아줘야 한다.
  
```java
//Controller

@Controller
@Log4j2
@RequiredArgsConstructor
public class GraphQLController {
  private final MemberService memberService;

  //Query Type

  /**
   * @Argument가 없을 시 넘어오는 Parameter를 인식하지 못하는 error가 있음
   *  - @Argument 는 @RequestBody, @RequestParam과 같은 인자값을 지정해줄 때 사용합니다.
   *
   * Error Msg : was not recognized by any resolver and there is no source/parent either.
   *             Please, refer to the documentation for the full list of supported parameters.
   * */
  @QueryMapping
  //public MemberDTO findMember(String email){  ☠️ Error  발생
  public MemberDTO findMember(@Argument String email){
    log.info("----- graphQL QueryType -----");
    return memberService.findMember(email);
  }
  
  ///////////////////////////////////////////////////

  /**
   * mutation{
   *   registerMember(
   *     memberEntity : {
   *      email : "edel1212@naver.com"
   *       name : "GrpahQlInsert"
   *       password :"123"
   *     }
   *   ){
   *     email
   *   }
   * }
   *
   * - Client에서 넘겨주는 ParameterID와 다를경우 Argument("Key!!")로
   *   설정하여 받을수있다! 아닐경우 값을 인식하지 못해 Error 발생
   *
   * Error Msg : was not recognized by any resolver and there is no source/parent either.
   *             Please, refer to the documentation for the full list of supported parameters.
   * */
  @MutationMapping
  //public String registerMember(@Argument MemberDTO memberDTO){ //인식을 못함 Client에서 보내는 Key 이름이 다름
  public String registerMember(@Argument("memberEntity") MemberDTO memberDTO){
    log.info("----- graphQL MutationType -----");
    return memberService.registerMember(memberDTO);
  }
  
}
```

<br/>

- 6 . Client 요청 및 grpahiql 사용
  -  http://myAddrss + /graphiql 로 접속하면 graphQL을 테스트할수 있는 페이지로 이동이 가능하다!

\- graphiql Test 🔽
```graphql
##grpahiql 

##Query Type##

# 단건 조회
query{
  findMember(email : "user94@naver.com"){
    # Type에 지정된 것을 원하는것을 추가 또는 제거하여 요청가능
    email
    password    
  }
}

# 다건 조회
query{
  allFindMembers{
    # Type에 지정된 것을 원하는것을 추가 또는 제거하여 요청가능
    email
    password    
  }
}


########################################################################

##Mutation Type##

# 등록
mutation{
  registerMember(memberEntity :{
    email : "yoojh@naver.com"
    password :  "123"
    name : "흑곰"
  })
}
  
# 수정  
mutation{
  updateMember(memberEntity :{
    email : "yoojh@naver.com"    
    name : "백곰!"
  })
}


#삭제
mutation{
  deleteMember(email: "yoojh@naver.com")
}  

```

<br/>

\- javascript Fetch-API Test 🔽
```javascript
//javascript 

/**
*  👉 Endpoint는 고정이므로 아래의 query를
*     원하는 요구사항에 맞게 끔 보내주면 된다. 
*/

const query = `query{
                      allFindMembers{
                        email
                        password
                      }
                    }`
fetch( "/graphql" ,
        {
          // 💬 POST 방식은 고정
          method: 'POST',
          // 💬 Content-Type 또한 json으로 고정
          headers: { 'Content-Type': 'application/json' },
          // stringify로 Query를 전달
          body: JSON.stringify({ query })
        }
      ).then(res => res.json())
       .then(json => console.log(json))
       .catch(err => console.log(err));

```