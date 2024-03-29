<h1>MappedSuperClass , Querydsl, 페이징 처리</h1>

<h3>1 ) MappedSuperClass 란❔</h3>

- 객체의 입장에서 <b>자주 반복</b>되거나 <b>공통 매핑 정보<b>가 필요할 때 사용한다.
- 직접 생성해서 사용할 일이 없으므로 <b>추상 클래스로 만드는 것을 권장</b>한다.
- 상속을 받아 사용하지만 <b>상속관계가 아니다.</b>
- 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용한다.

\- MappedSuperclass를 적용한 추상 Class🔽

```java
// abstract  class

/**
 * @Description : Entity 관련 작업중에 중복되는 작업들 Ex ) 등록시간 , 수정시간과 같은
 *                자동으로 추가되고 변경되어야 하는 컬럼들은 매번 프로그램안에서 처리하는
 *                일은 번거롭기에 사용한다.
 *
 *                ✔ @MappedSuperclass 가 지정된 클래스는 테이블로 생성되지 않는다.
 *                  - 실제 테이블은 해당 추상클래스를 상속한 Entity 의 class 가 생성된다.
 *
 *                ✔ @EntityListeners 란?
 *                  - 1) JPA Entity에서 이벤트가 발생할 때마다 특정 로직을 실행시킬 수
 *                      있는 어노테이션입니다. @EntityListeners(AuditingEntityListener.class)
 *                      즉, AuditingEntityListener 클래스가 callback listener로 지정되어
 *                      Entity에서 이벤트가 발생할 때마다 특정 로직을 수행하게 됩니다.
 *
 *                  - 3) JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는
 *                    역할은 AuditingEntityListener 로 이뤄진다.
 *
 *                  - 🎈 단!  JPA를 이용하면서  AuditingEntityListener 를 활성화 하기 위해서는
 *                      프로젝트에 @EnableJpaAuditing 설정을 "project"Application.java에
 *                      추가해줘야한다! [ JPA Auditing(감시, 감사) 기능을 활성화하기 위한
 *                      어노테이션입니다. ]
 *
 * */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter //Getter가 없으면 Entity -> DTO 변환 시 아래값에 접근을 못함 꼭 추가해주자.
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regeDate;

    @LastModifiedDate
    @Column(name ="moddate")
    private LocalDateTime modDate;

}

```

\- Application Main Start Class🔽

```java
//Main Class

@SpringBootApplication
@EnableJpaAuditing //AuditingEntityListener 활성화
public class GuestbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestbookApplication.class, args);
	}

}
```

\- Entity Class에 MappedSuperclass 적용🔽

```java
//Entity Class

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends  BaseEntity{ // BaseEntity를 상속 받음

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 자동생성
    private Long gno;

    @Column(length = 100,nullable = false)
    private String title;

    @Column(length= 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

}

```

\- 결과🔽

```log
<!-- console log -->
<!-- ===================================================================================
    Guestbook에는 작성,수정일 변수가 없지만 서버 기동 시 추가된 것을 확인할 수있다.
    @EntityListeners 사용으로 인해 Entity에 이벤트가 감지 될 경우
    @CreatedDate, @LastModifiedDate 설정이 되어있는 컬럼에 자동으로 값이 주입 또한
    가능하다.
===================================================================================    -->

Hibernate:

    create table tbl_guestbook (
       cnum bigint not null auto_increment,
        mod_date datetime(6),
        reg_date datetime(6),
        content varchar(1500) not null,
        title varchar(100) not null,
        writer varchar(50) not null,
        primary key (cnum)
    ) engine=InnoDB


<!-- ------------------------------------------------------------------- -->
<!-- Insert Test 시 -->
Hibernate:
    insert
    into
        tbl_guestbook
        (mod_date, reg_date, content, title, writer)
    values
        (?, ?, ?, ?, ?)
```

<hr/>

<h3>2 ) Querydsl ❔</h3>

- JPA에서 제공하는 Method 와 QueryMethod 그리고 @Query(JPQL) 사용해서도 많은 기능을 구현
  <br/>가능하지만 아쉽게도 선언할때 고정된 형태의 값을 가진다는 단점이있습니다.
  <br/>떄문에 복잡한 조합을 이용하는 경우의 수가 많은 상황에서는 동적 쿼리를 생성해서 처리가 가능한
  <br/> Querydss을 사용하면 좋다. [복잡한 검색조건, 조언, 서브쿼리 등 기능 구현이 가능함.]

- Querydsl을 이용하면 코드 내부에 맞는 쿼리를 생성할 수 있짐나 이를 위해서는 작성된 Entity Class를
  <br/>그대로 사용하지 않고 <b>"Q도메인"</b>을 이용해야한다.
- Querydsl를 사용하기 위해서는 Querydsl 라이브러리를 이용해서 Entity Class -> Q도메인 변환하는
  <br/>설정이 필요하다.

\-Querydsl 라이브러리 추가🔽

```gradle
//build.gradle

plugins {
	..code

	// 1. querydsl plugins 추가
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

dependencies {
	...code

	//2. Querydsl 라이브러리 추가
	implementation "com.querydsl:querydsl-jpa:5.0.0"
	implementation "com.querydsl:querydsl-apt:5.0.0"
}

/**
 * @Description : 설정 추가 -> gradle 빌드 -> compileQuerydsl{.. } 을 실행 하면
 *                 build > generated > querydsl > My_packageDir > entity
 * 	              Q + Entity Class 들이 생긴 것을 확인 할수있다.
 * 	              해당 Q도메인들은 개발자가 직접 건드리지 않는다
 *                 gradle 의 CompileQuerydsl 과 같은 task 를 통해서 자동으로 생성된다.
 *
 * 	              ✅ Querydsl을 사용 하기 위해선 사용하려는 jpaRepository에
 *                    QuerydslPredicateExecutor를 상속해줘야한다.
 *
 * */


// 3.  Querydsl 설정 추가

// querydsl에서 사용할 경로 설정
def querydslDir = "$buildDir/generated/querydsl"
// JPA 사용 여부와 사용할 경로를 설정
querydsl {
	jpa = true
	querydslSourcesDir = querydslDir
}
// build 시 사용할 sourceSet 추가
sourceSets {
	main.java.srcDir querydslDir
}
// querydsl 컴파일시 사용할 옵션 설정
compileQuerydsl{
	options.annotationProcessorPath = configurations.querydsl
}
// querydsl 이 compileClassPath 를 상속하도록 설정
configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
	querydsl.extendsFrom compileClasspath
}

```

\- QuerydslPredicateExcutor\<T> 추가🔽

```java
// JpaRepository

public interface GuestBookRepository extends JpaRepository<T, V>
    , QuerydslPredicateExecutor<T> { //추가해줘야 Queydsl이 사용 가능하다.
}
```

\- Querydsl 과 Query 사용 표시법 비교 정리 표🔽

<table border="1px" width="100%"> 
    <tr>
        <td>Querydsl</td>
        <td>Query문</td>
    </tr>
    <tr>
        <td>member.username.eq("member1")</td>
        <td>username = 'member1'</td>
    </tr>
    <tr>
        <td>member.username.ne("member1")</td>
        <td>username != 'member1'</td>
    </tr>
    <tr>
        <td>member.username.eq("member1").not()/td>
        <td>username != 'member1'</td>
    </tr>
    <tr>
        <td>member.username.isNotNull()</td>
        <td>이름이 is not null</td>
    </tr>
    <tr>
        <td>member.age.in(10, 20)</td>
        <td>age in (10,20)</td>
    </tr>
    <tr>
        <td>member.age.notIn(10, 20)</td>
        <td>age not in (10, 20)</td>
    </tr>
    <tr>
        <td>member.age.between(10,30)</td>
        <td>between 10, 30</td>
    </tr>
    <tr>
        <td>member.age.goe(30)</td>
        <td>age >= 30</td>
    </tr>
    <tr>
        <td>member.age.gt(30)</td>
        <td>age > 30</td>
    </tr>
    <tr>
        <td>member.age.loe(30)</td>
        <td>age <= 30</td>
    </tr>
    <tr>
        <td>member.age.lt(30)</td>
        <td>age < 30</td>
    </tr>
    <tr>
        <td>member.username.like("member%")</td>
        <td>like 검색</td>
    </tr>
    <tr>
        <td>member.username.contains("member")</td>
        <td>like ‘%member%’ 검색</td>
    </tr>
    <tr>
        <td>member.username.startsWith("member")</td>
        <td>like ‘member%’ 검색</td>
    </tr>
<table>

\- Querydsl 테스트🔽

```java
/**
 * @Description :  ✔ 1 ) 동적으로 처리하기 위해 Q도메인 Class를 얻어온다, Q도메인 Class를 이용하면
 *                      엔티티 클래스에 선언된 title, content 같은 필드 변수 활용이 가능해짐!
 *                      ---  쉽게 설명하면 객체 변수를 만들어 각각의 엔티티 변수에 접근하여
 *                           contains() 같은 조건에 맞는 메서드 사용이 가능
 *
 *                ✔ 2 ) BooleanBuilder는 Where문에 들어가는 조건을 넣어주는 컨테이너로 생각하면 된다.
 *                      --- 쉽게 설명하면 Where 문의 조건을 넣을 객체
 *
 *                ✔ 3) 원하는 조건을 만들어준다
 *                     해당 만들어준 조건의 객체 Type은 BooleanExpression 이어야한다
 *                      --- 2번에서 만든 builder에 넣어줘야는 객체
 *
 *                ✔ 4) 만들어진 조건은 where 문에 and 또는 or 등으로 합쳐준다.
 *                     --- 체이닝 가능함! 뒤에 계속 이어붙여서 조건을 만들어줄 수 있음
 *                      ✅ 중요 포인트는 해당 객체의 파라미터로는 Q도메인의 Predicate가 들어가는것이다
 *                          Java의 predicate 람다식이 아님!
 *
 *                ✔ 5) BooleanBuilder 는 GuestBookRepository 에 추가된
 *                     QuerydslPredicateExcutor 인터페이스에 의해 findAll()을 사용할 수있다.
 *
 *  - 결과값 : title에 "1"이 들어가있는 0페이지 ~ 10개 gnum으로 정렬 된 데이터
 * */
@Test
public void testQuery1(){
    Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

    QGuestbook qGuestbook = QGuestbook.guestbook; //1

    String keyword = "1";

    BooleanBuilder builder = new BooleanBuilder(); //2

    BooleanExpression expression = qGuestbook.title.contains(keyword); //3

    builder.and(expression); //4

    Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5

    result.stream().forEach(System.out::println);

}

/**
 * @Description  : 결과 값 : 제목 혹은 내용에 "1"이들어가고 gnum이 30보다 큰
 *                           0페이지의 10개의 목록 gnum Desc 정렬
 *
 * */
@Test
public void testQuery2(){
    Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());
    QGuestbook qGuestbook = QGuestbook.guestbook;

    String keyword = "1";

    BooleanBuilder booleanBuilder = new BooleanBuilder();

    BooleanExpression exTitle = qGuestbook.title.contains(keyword);

    BooleanExpression exContent = qGuestbook.content.contains(keyword);

    //두가지 조건을 or 조건으로 합침
    BooleanExpression exAll = exTitle.or(exContent); // 1-------------------------

    //조건식을 booleanBulider에 주입
    booleanBuilder.and(exAll); //2------------------------

    //gno 값이 0보다 크다(>) 라는 뜻
    booleanBuilder.and(qGuestbook.gno.gt(30L));//3------------------

    Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder,pageable);

    result.stream().forEach(System.out::println);
}
```

<hr/>
<br/>

\- ✅QuerydslRepositorySupport 사용방법 \_\_JPAQuertFactory이용 🎈[ 방법 1 ]🎈

--->Querydsl과 차이점❔

- Mybatis 사용시 Mapper Inferface 만 사용 해서 Mybaies를 이용 하는방법과
  <br/>DAO Interface, Impl을 생성 후 사용하는 방법이 있듯 사용 하는 방법의 차이가 있는 것이다.
- 개발환경에 따른 곳마다 사용하는 방식이 다르니 여러 방법에 대하여 알아두자!
- 장점으로는 아래와 같이 직접 Method형식으로 Chaining 하여 원하는 데이터를 가져올 수 있다는 것이다.

<br/>

\- Configuration Class 생성 및 JpaQueryFactory Bean등록🔽

```java
//Java - ConfingClass

@Configuration
public class QuerydslConfig {
    /**
     * PersistenceContext (영속성 컨텍스트) 지정
     * */
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

}

```

\- QueryFactory를 사용할 SupoprtClass 추가🔽

- 🎈중요한점
- 1 ) JpaRepository Interface도 있어야 한다.
- 2 ) 해당 class는 Interface가 ❌
- 3 ) @Repository 필수이다

```java
//QuerydslRepositorySupport Class

@Repository
public class GuestBookRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public GuestBookRepositorySupport(JPAQueryFactory queryFactory) {
        super(GuestBook.class);
        this.queryFactory = queryFactory;
    }

    public List<GuestBook> findByName(Long gunm) {
        QGuestBook qGuestBook = QGuestBook.guestBook;
        return queryFactory
                .selectFrom(qGuestBook)
                .where(qGuestBook.gnum.goe(gunm))
                .fetch();
    }

}
```

\- QueryFactory Test Code🔽

```java
//JUnit Test Code -- QueryFactory

@Autowired
private GuestBookRepositorySupport guestBookRepositorySupport;

@Test
public void jpaFactoryUse(){
    List<GuestBook> r = guestBookRepositorySupport.findByName(100L);
    log.info("Result :: {}", Arrays.toString(r.toArray()));
}

```

<br/>

\- ✅QuerydslRepositorySupport 사용방법 \_\_JPQLQuery이용 🎈[ 방법 2 ]🎈

- [방법1]과 차이점❔
- 1. 따로 @Configuration Class를 지정할 필요가 없다.
- 2. JPAQueryFactory를 사용하지 않으므로 불러와 사용할 필요가 없다.
- 3. 대신 JPQLQuery 객체를 이용한다.
- 4. 기존 Repository에 Interface를 확장하는 방석이다.
- 5. [방법1] 보다 해당 방법이 좀 더 간단하고 효율적이로 보인다.
- 6. SearchBoardRepository만으로 독릭접으로 사용이 불가능하다. 상속 없이 주입 후 사용 시 Error 발생
     <br/> ErrorMsg : Error creating bean with name... No qualifying bean of type ... single matching bean but found
     <br/>

\- 기능 확장에 필요한 Interface 및 Impl 추가🔽

```java
//java - Interface

public interface SearchBoardRepository {

    Board search1();
}


////////////////////////////////////////////////////////////


//java - Interface Implements
/**
*  1 . QuerydslRepositorySupport를 상속 받는다.
 * 2. 부모 Class 인 QuerydslRepositorySupport 에 생성자가 요구하는 값을 추가해준다.
 *    -> Class를 요구하는데 내가 원하는 <strong> 데이터 주체의 Entity Class 이다!</strong>
 * 3. 구현하고자 하는 interface를 impl 시킨 후 구현해준다.
* */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport
                                            implements SearchBoardRepository {

    //생성자 메서드 작성 - QuerydslRepositorySupport가 요구함
    public SearchBoardRepositoryImpl() {
        //내가 사용할 Entity Class
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("Querydsl Support Search Method!!! ");

        //1 . Q도메인 생성
        QBoard board = QBoard.board;

        //2. 사용할 대상을 JPQLQuery으로 지정
        JPQLQuery<Board> jpqlQuery = from(board);

        //3. 필요 질의 조건 추가
        jpqlQuery.select(board).where(board.bno.eq(100L));

        //fetch()를 통해 데이터 빈환
        List<Board> result = jpqlQuery.fetch();

        return null;
    }
}
```

<br/>

\- 상위에 만든 확장용 Repository을 대상 Repository에 상속해준다🔽

```java
//java -JpaRepository

// SearchBoardRepository [ QuerydslSupport Interface 상속 추가 ]
public interface BoardRepository extends JpaRepository<Board,Long> , SearchBoardRepository {
}

```

<br/>

\- JPQLQuery Test Code🔽

```java
//java -JUnit Test Code

@SpringBootTest
@Log4j2
public class BoardRepositoryWithQuerydslSupportTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void getListWithSupportImpl(){
        //상속 대상의 Method Call
        boardRepository.search1();
    }

}
```

<hr/>

<h3>3 ) 서비스 계층과 DTO ❔</h3>

- Entity 객체를 영속 계층 바깥쪽에서 사용한는 것보다 DTO(Data Transfer Objct)를 이용하는것을 권장함.
- Entity 객체는 단순히 데이터를 담느 객체가 아니라 실제 DB와 관련이 있고, 내부적으로 Entity Manager가
  <br/>관리하는 객체이기 때문이다. -> 생명주기가 완전히 다르기에 분리해서 사용하는것을 권장함.
- DTO는 쉽게 설명하면 각 계층 끼리 주고 받는 상자 개념으로 보면 좋다.
  <br/>읽고 쓰는것이 모두 허용되고 일회성으로 사용되는 성격이 강합니다
- DTO를 사용하게 되면 Entity 객체의 범위를 한정 지을 수 있기 때문에 좀 더 안전한 코드를 작성할 수 있고
  <br/>화면과 데이터를 분리한다는 취지에도 좀 더 적합합니다.

\- DTO Class🔽

```java
//java - DTO Class

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data //자유롭게 데이터를 변경할 수 있게 하기 위함.
public class GuestbookDTO {

    private Long gno;
    private String title;
    private String content;
    private String writer;
    //BaseEntity에 추가된 변수
    private LocalDateTime regDate,modDate;

}

```

\- Service [ default Method 사용 ], ServiceImpl [dtoToEntity Method]🔽

```java
//Serivce - Interface

public interface GuestbookService {
    /**
     * @Description : Entity 객체를 DTO 객체로 변환하기 위해 사용함! - Java8 버전 부터 추가된
     *                defualt를 사용 기존에 추상 클래스를 통해서 전달해야하는 실제 코드를
     *                인터페이스에 선언이 가능해짐
     *
     *                - ModelMapper 혹은 MaoStruct 등의 라이브러리를 사용할 수도 있지만
     *                - 해당 예제에서는 default method 를 사용하여 직접 처리함
     *
     *                - DTO(Data Transfer Object)로 변환하는 이유 ?
     *                   1) DTO 는 Entity 객체와 달리 각 계층끼리 주고받는 우편물이나 상자의 개념
     *
     *                   2) JPA를 이용하게되면 엔티티 객체는 단순히 데이터를 담는 객체가 아니기에
     *                      이것을 분리하기위해 사용
     *
     *                   3) DTO는 일회성으로 데이터를 주고받는 용도지만 Entity 객체는 엔티티메니저가
     *                      데이터를 관리함 따라서 생명주기도 전혀 다름!
     * */
    //DTO - > Entity
    default GuestBook dtoToEntity(GuestBookDTO dto){
        return GuestBook.builder()
                .gnum(dto.getGnum())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
    }

    //Entity - > DTO
    default  GuestbookDTO entityToDto(Guestbook entity){
        return GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegeDate())
                .modDate(entity.getModDate())
                .build();
    }
}


///////////////////////////////////////////////

//Serivce - Implements
@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        Guestbook entity = this.dtoToEntity(dto); //Servcie -  default Method
        repository.save(entity);
        //repository 에서 save 될 시 gno 가 생성된다. 그것을 반환함
        return entity.getGno();
    }
}
```

<hr/>
<br/>
<h3>4 ) 페이징 처리</h3>

\- 페이지 목록 처리를 위한 DTO🔽

```java
//java

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page; //현재 페이지
    private int size; //목록의 개수

    //검색조건
    private String type;
    private String keyword;

    //NoArgsConstructor
    //- 값이 없을 경우 default 값 지정
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    //Pageable 객체를 생성하는 매개변수로 Sort 값을 받는다.
    public Pageable getPageable(Sort sort){
        return PageRequest.of(this.page -1, this.size , sort);
    }

}

```

\- 페이지 목록 처리를 위한 DTO🔽

```java
//java -- PageResultDTO

@Data
public class PageResultDTO <DTO, EN>{ // Generic으로 DTO와, Entuty를 받음
    //DTO list       - 데이티 목록
    private List<DTO> dtoList;

    //Total Page Num  - 총 페이지번호 개수
    private int totalPage;

    //Now page Num    - 현재 페이지번호
    private int page;
    //List size       - 한번에 보여줄 목록개수
    private int size;

    //start Page Num  - 시작페이지 번호
    private int start;
    //end Page Num    - 종료페이지 번호
    private int end;

    //next flag       - 이전페이지 버튼 유,무
    boolean prev;
    //prev flag       - 다음페이지 버튼 유,무
    boolean next;

    //page Num List
    private List<Integer> pageList;

    //생성자로는 Page 와 처리할 Function을 매개변수로 받는다
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        //목록 지정
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        //totalPage 지정
        this.totalPage = result.getTotalPages();

        //페이징 계산 핵심 로직
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1 ;  // 0부터 시작하므로 1을 더해 줌 [View에서 봐야 하므로]
        this.size = pageable.getPageSize();         // 현재 페이지 Size 세팅

        /**
         * temp end page [ End Page 값을 구한다. ]
         * 현재 page / 10.0 -> 그 값을 올림  ->  목록Size을 곱해 준다.
         * 1   페이지 경우  MathCeil(0.1) * 10 = 10
         * 10  페이지 경우  MathCeil(1)   * 10 = 10
         * 100 페이지 경우  MathCeil(10)  * 10 = 100
         */
        int tempEnd = (int) Math.ceil(this.page/ 10.0) * this.size;

        // 시작 페이지 번호는 마지막페이지 번호에서 (목록사이즈 - 1)를 뺀 값임
        start = tempEnd - (this.size - 1) ;

        //이전 버튼 : 페이지시작 번호가 1보다 클 경우 True
        prev = start > 1;

        /**
         * 마지막 번호는  total Page 와 tempEnd 를 비교하여 작은 수로 지정 이유는
         * ex)
         *  totalNum " 35" 가  totalPage 지만
         *  tempEnd 의 경우 현재 내가 고른 페이지의 번호가 33 일경우 40으로 계산되기
         *  때문에 정합성이 맞지 않음. [ Start Page 를 구하는게 주목적이기 떄문]
         *  - - - - - - - - -
         *  간단하게 말하면
         *  - 둘중 작은걸 쓰면 된다.
         *    tmpEnd의 경우는 무조건 자리수에 맞춰 계산하기에
         *   잘못된 수가 나오기 때문이다.
         */
        // this.totalPage > tmEnd ? tmpEnd : this.totalPage
        end = Math.min(this.totalPage, tempEnd);

        /**
         * 현재 페이지가 tempEnd 보다 많을경우 True
         * RealTotalPage = 13인데
         * tempEnd 는 10 일경우 당연히 3개의 페이지 개수가
         * 더 있기 때문임!
         */
        next = this.totalPage > tempEnd;

        this.pageList = IntStream.rangeClosed(start,end).boxed()
                .collect(Collectors.toList());
    }
}
```

\- 페이지 목록 처리 Service, ServiceImpl🔽

- Service🔽

```java
//java - Servcie Inferface

public interface GuestbookService {

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    code...
}
```

- ServiceImpl🔽

```java
//java - Service Implements

@Service
@RequiredArgsConstructor
@Log4j2
public class GuestbookServiceImpl implements GuestbookService{

    // 기본 페이징 조회
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        //1 . Pageable 객체 생성 : PageRequestDTO -> getPageable() 사용
        //                        반환 return PageRequest.of()이기에 사용 가능!
        Pageable pageable = requestDTO.getPageable(Sort.by("gno"));

        //2 . pageable 조건으로 Data 조회.
        Page<Guestbook> result = repository.findAll(
            this.getSearch(requestDTO)   //검색 조건 메서드
            , pageable);                 //페이징 객체

        //3. Functional을 사용해서 Entity -> DTO 함수생성
        Function<Guestbook, GuestbookDTO> fn = this::entityToDto;
        //4. PageResultDTO 객체를 만들어 전달
        // 파라미터로는 (Page결과, Entity -> DTO 전환 함수)가 필요함
        return new PageResultDTO<GuestbookDTO, Guestbook> (result,fn);
    }


/////////////////////////////////////////////////////////////////////////////////////


    //검색조건을 추가한 조회 Method[querydls 사용]
    /**
     * @Description  : 동적 검색 조건이 처리되는 경우 실제 코드는 Querydsl 을 통해
     *                 BooleanBuilder 를 사용하여 작성하고 {@link GuestbookRepository}에서는
     *                 QueryDsl 로 작성된 BooleanBuilder 를 사용하여 findAll()로 처리한다
     *
     *                 ✔ BooleanBuilder 에 대한 작성은 별도의 Class를 사용하여 처리할 수있지만
     *                   간단하게 해당 ServiceImpl 에서 Method 를 작성하여 사용함
     * */
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(type == null){ //검색 조건이 없을 경우
            return booleanBuilder;
        }

        QGuestbook qGuestbook = QGuestbook.guestbook;

        //검색 조건이 있을 경우 조건 및 keyword 를 추가!
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.equals("t")) {
            booleanBuilder.or(qGuestbook.title.contains(keyword));
        }
        if (type.equals("c")) {
            booleanBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.equals("w")) {
            booleanBuilder.or(qGuestbook.writer.contains(keyword));
        }

        return booleanBuilder;
    }
}
```

\- 페이지 목록 처리 Test🔽

```java
//JUnit Test Code
@Test
public void testList(){
    //1 . PageRequestDTO 객체 생성
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(1).size(10).build();

    //2. 데이터 요청 - 내부 로직은 SerivceImpl 확인!
    PageResultDTO<GuestBookDTO, GuestBook> resultDTO =
            guestbookService.getList(pageRequestDTO);

    log.info(resultDTO.getDtoList());
}

```

<h3>5 ) @ModelAttribute 란❔</h3>

- 객체형태로 매핑되어 Model로 따로 넘겨주지 않아도된다.
- Default 생성자가 있을 시 그에 맞는 데이터가 나온다
- 해당 객체에 알맞은 값을 넘겨주면 그에 맞게 적용되어 사용 가능하다.
- 값을 가지고 다니면서 활용하기 좋음

\- ModelAttribute Test Controller🔽

```java
//java - Controller

@Controller
@RequestMapping("/paging")
@Log4j2
@RequiredArgsConstructor
public class PagingController {
    @GetMapping("/test")
    public void modelAttributeTest(@ModelAttribute("modelTestData") PageRequestDTO dto){
        // 값을 안넘겨줄경우 : PageRequestDTO(page=1, size=10);
        //--------------------------------------------------------------
        // 값을 넘겨줄경우 [http://localhost:8080/paging/test?page=100]
        //             : PageRequestDTO(page=100, size=10);
        log.info("dto :: {}", dto);
    }
}

/////////////////////////////////////////////

//redirect를 섞어서 사용
@PostMapping("/direct")
public String modelAttrWithRedirect(@ModelAttribute("modelTestData") PageRequestDTO dto
            , RedirectAttributes redirectAttributes){
    redirectAttributes.addFlashAttribute("page"    , dto.getPage());
    redirectAttributes.addFlashAttribute("keyword" , dto.getKeyword());
    redirectAttributes.addFlashAttribute("type"    , dto.getType());
    return "redirect:/paging/view";
}
```

\- ModelAttribute Test Html🔽

```html
<!-- Html -->

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    [[${modelTestData}]]
  </body>
</html>
```
