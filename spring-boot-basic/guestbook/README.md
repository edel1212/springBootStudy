# MappedSuperClass , Querydsl, Paging

## 1 ) MappedSuperClass 란❔
- **자주 반복**되거나 **공통 매핑 정보**가 필요할 때 사용
  - 등록일, 수정일, 등록자, 수정자 와 같은 공통 정보에 사용
- 직접 생성해서 사용할 일이 없으므로 **추상 클래스**로 **만드는 것을 권장**

### Abstract Class
- `@MappedSuperclass` 가 지정된 클래스는 **테이블로 생성되지 않음**
  - 해당 필드는 추상클래스를 상속한 Entity에서 생성
- `@EntityListeners` 란?
  - JPA 엔티티에서 **특정 이벤트**(예: persist, update, remove 등)가 발생할 때마다 미리 정의된 로직(콜백 메서드)을 **실행하기 위해 사용되는 어노테이션**이다.
    - 엔티티에 대한 라이프사이클 이벤트를 감지하고, 이를 기반으로 추가적인 작업(예: 감사 로깅, 자동 필드 업데이트 등)을 수행
    - ✨ AuditingEntityListener 를 활성화 하기 위해서는  `@EnableJpaAuditing` **설정 필요**
      - `"project"Application.java`에 추가 필요
```java
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
### Application Main Start Class
- `@EnableJpaAuditing`를 통한 **AuditingEntityListener 활성화**
```java
@SpringBootApplication
@EnableJpaAuditing 
public class GuestbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestbookApplication.class, args);
	}

}
```

### MappedSuperclass 적용
- extends를 통해 작성한 **추상 클래스 상속**
```java
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends  BaseEntity{ // BaseEntity를 상속 받음
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Long gno;
}
```


## 2 ) Querydsl ❔

```properties
# ℹ️ SpringBoot 3.x.x 버전 이후로 적용 방법이 변경 되었음
```

### Dependencies

#### 2.x.x 버전 적용 방법

```groovy
//build.gradle

plugins {
	// 1. querydsl plugins 추가
	id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

dependencies {
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

#### 3.x.x 버전 적용 방법
![img.png](img.png)
- dependencies 추가 후  `Task -> build` 진행
  - 프로젝트 -> build -> generated에 Q 클래스가 생성 확인
```groovy
dependencies {
	// queryDSL
	implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}
```

### Querydsl 과 Query 사용 비교

| Querydsl                           | Query문                      |
|------------------------------------|------------------------------|
| `member.username.eq("member1")`    | `username = 'member1'`       |
| `member.username.ne("member1")`    | `username != 'member1'`      |
| `member.username.eq("member1").not()` | `username != 'member1'`      |
| `member.username.isNotNull()`      | 이름이 `is not null`         |
| `member.age.in(10, 20)`            | `age in (10, 20)`            |
| `member.age.notIn(10, 20)`         | `age not in (10, 20)`        |
| `member.age.between(10, 30)`       | `between 10, 30`             |
| `member.age.goe(30)`               | `age >= 30`                  |
| `member.age.gt(30)`                | `age > 30`                   |
| `member.age.loe(30)`               | `age <= 30`                  |
| `member.age.lt(30)`                | `age < 30`                   |
| `member.username.like("member%")`  | `like` 검색                  |
| `member.username.contains("member")` | `like '%member%'` 검색      |
| `member.username.startsWith("member")` | `like 'member%'` 검색     |


### QuerydslPredicateExcutor<T> 
- Jpa에서 제공하는 `findAll()` 함수 내 **BooleanBuilder**를 **사용하기 위해서는 필요**함
  - `guestbookRepository.findAll(builder, pageable)`
- QuerydslPredicateExecutor를 상속하지 않더라도 Qureydsl **사용은 가능**  

#### Repository
```java
public interface GuestBookRepository extends JpaRepository<T, V>, QuerydslPredicateExecutor<T> { 
}
```

#### QuerydslPredicateExcutor 사용 Test

```java
class TestCode{
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
}
```

### QuerydslRepositorySupport 설정 방법


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

## 3 ) 페이징 처리

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




<h3>6 ) QuerydslRepositorySupport의 JQPLQuery 사용 Join 및 Object[] 다루기</h3>
<strong>✅확인사항</strong>

- 1 ) Querydsl 설정은 필수
- 2 ) QuerydslRepositorySuppoert은 2가지 방법이 존재( JPAQueryFactory, JPQLQuery ) [ 차이 확인 - Click]("https://github.com/edel1212/springBootStudy/tree/main/guestbook")
  <br/>

\- RepositorySupport [ JPQLQuery ] 설정 및 Test Code🔽

```java
//java - RepositorySupport(Interface) , impl

// Interface - SearchBoardRepository
// 해당  Interface는 구현체가 필요하다!
public interface SearchBoardRepository {
    Board search1();
}


/////////////////////////////////////////////////////////////////////////////////


// Interface - SearchBoardRepository(impl)
/**
*  1 . QuerydslRepositorySupport를 상속 받는다.
 * 2. 부모 Class 인 QuerydslRepositorySupport 에 생성자가 요구하는 값을 추가해준다.
 *    -> Class를 요구하는데 내가 원하는 <strong> 데이터 주체의 Entity Class 이다!</strong>
 * 3. 구현하고자 하는 interface를 impl 시킨 후 추상화 메서드를 구현해준다.
* */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

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


/////////////////////////////////////////////////////////////////////////////////


//JpaRepository [상속을 받아야 한다]
// SearchBoardRepository [ QuerydslSupport Interface 상속 추가 ]
public interface BoardRepository extends JpaRepository<Board,Long>
                                  , SearchBoardRepository {
  //...code...
}


/////////////////////////////////////////////////////////////////////////////////


//JUnit Test Code

@SpringBootTest
@Log4j2
public class BoardRepositoryWithQuerydslSupportTest {
    @Autowired
    private BoardRepository boardRepository;

    /**
    * 아래와 같이 사용하면 Error 발생 단독으로 사용 불가능함.
    * Bean을 따로  추가해줘 Error 발생함.
    * Error Msg : Error creating bean with name .........
    *              Unsatisfied dependency expressed through field
    *
    * 테스트 내용 :  대상 JpaRepository에 따로 설정을 하는 방식으로
    *               구현하려 하였으나
    *               - JpaRepository(Interface)
    *               - QuerydslRepositorySuppor(Class)
    *               이므로 상속 자체가 불가능함 사용하려면
    *                Support(Interface, Impl) 구현 후
    *               JpaRepository에 상속해주는 방식으로 구현할 수 밖에 없을 듯함.
    */
    //@Autowired
    //private SearchBoardRepository searchBoardRepository;

    //❌Error
    @Test
    public void getListWithSupportImpl2Error() {
       // searchBoardRepository.search1();
    }

    //✅정상
    @Test
    public void getListWithSupportImpl2() {
        boardRepository.search1();
    }
}

```

<br/>

\- JPQLQuery - LEFT JOIN (ON) [✅Tuple 사용]🔽
<br/> 🎈중요사항
<br/> - 반환 값이 객체가 아닌 각각의 데이터를 받아올 경우 Generic에는 Tuple라는 객체를 사용해야함!
<br> ex) List<Tuple>

```java
//java - SupportService , impl

// Interface - SearchBoardRepository
public interface SearchBoardRepository {
    Board search2WithJoin();
}


/////////////////////////////////////////////////////////////////


//SearchBoardRepository - Impl
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements
    SearchBoardRepository {

    //[ Error는 없으나 데이터에 접근 ❌ ]
    @Override
    public Board search2WithJoin() {

        log.info("-------------------------");

        //1 . Q도메인 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JPQLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);
        // 🎈 중요 : JPQL과 다른 점은 join에  연관관계가 있을 경우에도 on 으로 조건이 필요하다.
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        jpqlQuery.select(board, member.email, reply.count()).groupBy(board);

        log.info("----------------------------------------");
        log.info(jpqlQuery);
        log.info("----------------------------------------");

        /*
          🎈🎈 중요 🎈🎈
          해당 쿼리문은 객체 단위가 아닌
          각각의 데이터를 추출하는 경우에는
          Tuple을 사용해야한다! 그 이유는 데이터에 접근이 불가능함..
        */
        List<Board> result = jpqlQuery.fetch();

        log.info("result :: {}", result);
        /**
          * log : result :: [
            [Board(bno=1, title=Title..1, content=Content...1), user1@naver.com, 300]
            , [Board(bno=2, title=Title..2, content=Content...2), user2@naver.com, 0]
            , [Board(bno=3, title=Title..3, content=Content...3), user3@naver.com, 0]
            ....
        */

        // Ex) result.get(0).Board안의 getter만 접근 가능하다는 문제가있음

        return null;
    }


    //[ 👍👍 <Tuple>로 받아 데이터 접근이 가능해짐 👍👍 ]
    @Override
    public Board search2WithJoinAddTuple() {

        log.info("-------------------------");

        //1 . Q도메인 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JPQLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);
        // 🎈 중요 : JPQL과 다른 점은 join에  연관관계가 있을 경우에도 on 으로 조건이 필요하다.
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member,  reply.count()).groupBy(board);

        List<Tuple> result = tuple.fetch();

        //<Tuple>을 사용 시
        //result.get(0).get(??) 이런식으로 접근이 가능해짐! 👍

        log.info("result :: {}", result);

        return null;
    }
}
```

<br/>

\- JPQLQuery를 사용하여 Page\<Object[]\> 처리[ ✅검색어 조건 추가 ]🔽

```java
//java - SupportService , impl

// Interface - SearchBoardRepository
public interface SearchBoardRepository {
    // 파라미터로 PageRequestDTO를 사용하지 않는 이유는
    // Repository에서 DTO의 사용의도를 생각해보면 쉽다
    // DTO(Data Transfer Object)란
    // 말 그대로 계층간 데이터 교환을 위해 사용하는
    // 객체(Java Beans) 이기에 취지에 적합하지 않기 때문이다.
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}


///////////////////////////////////////////////////////////////////////////////


// java - SearchBoardRepositoryImpl
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements
                                                              SearchBoardRepository {

  ..code...

  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
    log.info("Search--------------------------");

    //1. Q도메인 객체 생성
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    //2. JQPLQuery 객체 생성
    JPQLQuery<Board> jpqlQuery = from(board);

    /**
     * ❌ 삽질함 ..
     * ErrorMsg : No data type for node...
     *  이유 : jpqlQuery.leftJoin(board).on(board.writer.eq(member));
     *  수정 : jpqlQuery.leftJoin(member).on(board.writer.eq(member));
     *
     *  원인분석 : 이미 JQPLQuery 객체를 만들때 기준을 board로 잡았는데
     *            join에 또 board를 할필요가없다..
     *            ex) JPQLQuery<Board> jpqlQuery = from(board);
     * */
    //3. Join 추가
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    //4. Generic을 Tuple로 변경 - 반환값의 객체가 board뿐만이 아니기 떄문임.
    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

    //3. 인자로 받아온 조건 추가 - BooleanBuilder, BooleanExpression
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(board.bno.gt(0L)); // bno가 0보다 크고

    //4. type 체크 알맞은 조건 추가
    if(type != null){
        if(type.contains("t")) booleanBuilder.or(board.title.contains(keyword));
        if(type.contains("w")) booleanBuilder.or(member.email.contains(keyword));
        if(type.contains("c")) booleanBuilder.or(board.content.contains(keyword));
    }//if

    //5. 3~4번에서 만들어준 조건을 typle에 추가
    tuple.where(booleanBuilder);

    //6. count를 사용했으니 board에 groupBy 시켜줌
    tuple.groupBy(board);

    //7. 타입을 List<Tuple>로 변경
    List<Tuple> result = tuple.fetch();

    log.info("result :: {}", result);


    return null;
  }
}


///////////////////////////////////////////////////////////////////////////////


//JUnit Test Code - Tuple Paging [ With SearchOption ]
@SpringBootTest
@Log4j2
public class SupportQueryTests {
  @Test
    @Description("Tuple Test")
    public void testSearchPage(){
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        // Parent Interface에서 상속[구현] 받은 Method를 사용!
        //  List가 아니라 Page이다!!
        Page<Object[]> result = boardRepository.searchPage("tw","1",pageable);

        /* Result Query
          Hibernate:
            select
                board0_.bno as col_0_0_,
                member1_.email as col_1_0_,
                count(reply2_.rno) as col_2_0_,
                board0_.bno as bno1_0_0_,
                member1_.email as email1_1_1_,
                board0_.mod_date as mod_date2_0_0_,
                board0_.reg_date as reg_date3_0_0_,
                board0_.content as content4_0_0_,
                board0_.title as title5_0_0_,
                board0_.writer_email as writer_e6_0_0_,
                member1_.mod_date as mod_date2_1_1_,
                member1_.reg_date as reg_date3_1_1_,
                member1_.name as name4_1_1_,
                member1_.password as password5_1_1_
            from
                board board0_
            left outer join
                member member1_
                    on (
                        board0_.writer_email=member1_.email
                    )
            left outer join
                reply reply2_
                    on (
                        reply2_.board_bno=board0_.bno
                    )
            where
                board0_.bno>?
                or board0_.title like ? escape '!'
                or member1_.email like ? escape '!'
            group by
                board0_.bno
        */
    }
}
```

<br/>

\- JPQLQuery - sort/count 처리🔽

> 🎈 Pageable의 orderBy() 값을 사용할수 없는 이유 ❔

- 1 . OrderBy의 타입이 다르기 때문에 어쩔수가 없다.
- 2 . tuple.orderBy(pageable.getSort()); // 요구하는 파라미터 타입이 달라 사용이 불가능함
  <br/> Error Msg : Cannot resolve method 'orderBy(org.springframework.data.domain.Sort)'

> 🎈 return 시 new PageImpl(...)이며 paging 에 필요한 값을 추가해 줘야한다.

```java
//java

//SearchBoardRepository - Skip...


/////////////////////////////////////////////////////////////////////


//SearchBoardRepository - Impl
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements   SearchBoardRepository {
  @Override
  public Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable) {

      ...code... //상단의 searchPage()와 같은 코드임

      //5. 3~4번에서 만들어준 조건을 typle에 추가
      tuple.where(booleanBuilder);

      ////////////////////////////////////////////////////////////////////////////
      // - 정렬 추가 -  //
      // 👿 문제 발생
      //tuple.orderBy(pageable.getSort()); ❌  Parameter가 다르므로 사용 불가능 !!

      /**
      * @Description  : 정렬 시 문제
      *                 해당 Method 의 파라미터로 pageable 을 받아 JPQLQuery의 orderBy()로
      *                 처리하면 될것 같지만 JPQL 에서는 Pageable 의 Sort 객체를 지원하지
      *                 않는 다는 문제가 있다.
      *
      *                 ✔ 따라서 orderBy()의 경우 OderSpecifier<T extends Comparable>로 처리해야함
      *                 ✔ 해당 OderSpecifier 에서 매개변수의 타입은 2가지인데
      *                    - Order, Expression 인데 둘다 타입은 querydsl.core... 쪽이다.
      *
      *                 ❔ 그럼 왜 pageable 을 파라미터로 받았는가?
      *                    - sort 객체의 값을 forEach()를 사용해서 OderSpecifier 를 만들기 위해.
      *                    - JPQLQuery 의 offset() 과 limit()를 이용하여 페이지를 처리하기 위함.
      *
      *
      *
      *
      * **/


      // ✅ 해결 방법
      // Pageable에서 받아온 sort 을 사용하여 OrderSpecifier 객체를 만들어 추가해준다.

      Sort sort = pageable.getSort();
      //정렬 조건이 여러개일수 있기에 forEach 사용
      sort.stream().forEach( order->{
          // i. 정렬 조건 값을 가져옴
          String prop = order.getProperty();
          // ii. parable에서 정렬 방식 확인 후 Order 객체 생성
          Order direction = order.isAscending() ? Order.ASC : Order.DESC;
          // iii. PathBuilder 객체 생성 - [ 🎈주의 : Generic으로는 Entity Class를 사용해야한다! ]
          PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class,"board");
          // iv. orderBy()에 맞는 Parameter 사용
          tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

      });

      ////////////////////////////////////////////////////////////////////////////

      //6. count를 사용했으니 board에 groupBy 시켜줌
      tuple.groupBy(board);

      //7. Paging을 위한 offset값 과 limit 추가
      tuple.offset(pageable.getOffset());
      tuple.limit(pageable.getPageSize());

      //8. 타입을 List<Tuple>로 변경
      List<Tuple> result = tuple.fetch();
      log.info("result :: {}", result);

      //9. count 변수 추가 및 값 주입 [Paging을 위함] - 해당값을 구핼때 따로 count를
      //   구하는 Query가 돌아감!
      long count = tuple.fetchCount();
      log.info("count :: {}",count);

      //10.반환 타입은 PageImpl<Object[]> 형식으로 반환하며
      //   위에서 얻은 fetch 데이터와 pageable, count를 인자값으로 넘겨주자 [ 다형성 ]
      //
      //주의 사항 : PageImpl 객체 생성 시 List<Tuple>로 전달하는데
      //          이것을 Array로 변환해서 넘겨줘야한다!
      //          그래야 한곳에서 조작해서 넘기기에 다음 단계에서 또다시 변환 해줄
      //          필요가 없어서 편함!
      return new PageImpl(result.stream()
                                .map(Tuple::toArray)
                                .collect(Collectors.toList())
                          , pageable
                          , count);
      /** ---- Result Query ----
            Hibernate:
            select
                board0_.bno as col_0_0_,
                member1_.email as col_1_0_,
                count(reply2_.rno) as col_2_0_,
                board0_.bno as bno1_0_0_,
                member1_.email as email1_1_1_,
                board0_.mod_date as mod_date2_0_0_,
                board0_.reg_date as reg_date3_0_0_,
                board0_.content as content4_0_0_,
                board0_.title as title5_0_0_,
                board0_.writer_email as writer_e6_0_0_,
                member1_.mod_date as mod_date2_1_1_,
                member1_.reg_date as reg_date3_1_1_,
                member1_.name as name4_1_1_,
                member1_.password as password5_1_1_
            from
                board board0_
            left outer join
                member member1_
                    on (
                        board0_.writer_email=member1_.email
                    )
            left outer join
                reply reply2_
                    on (
                        reply2_.board_bno=board0_.bno
                    )
            where
                board0_.bno>?
                or board0_.title like ? escape '!'
                or member1_.email like ? escape '!'
            group by
                board0_.bno
            order by
                board0_.bno desc limit ?

        ----------------------------------

        Hibernate:
            select
                count(distinct board0_.bno) as col_0_0_
            from
                board board0_
            left outer join
                member member1_
                    on (
                        board0_.writer_email=member1_.email
                    )
            left outer join
                reply reply2_
                    on (
                        reply2_.board_bno=board0_.bno
                    )
            where
                board0_.bno>?
                or board0_.title like ? escape '!'
                or member1_.email like ? escape '!'
        */
  }

}


```

\- JPQLQuery - Paging (JUnit Test)🔽

```java
//java

// JUnit Test - JQPLQuery [Paging]
@SpringBootTest
@Log4j2
public class BoardServiceTests {
  @Autowired
  private BoardService boardService;

  @Test
    @Description("JPQLQuery Sort And Search option")
    public void testSearchWithSortAndKeyword(){
        // 1. PageRequestDTO 객채생성
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setType("twc");
        pageRequestDTO.setKeyword("1");

        // 2. Service 호출
        PageResultDTO<BoardDTO, Object[]> result = boardService.getListWithJQPLQuery(pageRequestDTO);
        result.getDtoList().forEach(log::info);
    }
}


///////////////////////////////////////////////////////////


// Serivce - Interface
public interface BoardService {
    PageResultDTO<BoardDTO, Object[]> getListWithJQPLQuery(PageRequestDTO requestDTO);

    //DTO 변환 시
    //각각해당하는 부분에 데이터를 주입하기위해서
    // 3개의 파라미터가 필요하다[ Board, Member, Long ]
    default BoardDTO entityToDTO(Board board, Member member,Long replyCnt){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCnt.intValue())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }
}


///////////////////////////////////////////////////////////


// Serivce - Implements
@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  private final BoardRepository boardRepository;

  @Override
  public PageResultDTO<BoardDTO, Object[]> getListWithJQPLQuery(PageRequestDTO requestDTO) {

      log.info("requestDTO ::: {}",requestDTO);

      // 1. Functional 변수 생성 - 해당 entity-> DTO 메서는 service에 정의되어 있음
      Function<Object[], BoardDTO> fn = en -> this.entityToDTO( (Board) en[0]
                                                              , (Member)en[1]
                                                              , (Long) en[2]);

      // 2 . JPQLQuery를 적용한 Method 호출
      Page<Object[]> result = boardRepository.searchPageWithSort(requestDTO.getType()
              , requestDTO.getKeyword()
              ,requestDTO.getPageable(Sort.by("bno").descending()) );

      return new PageResultDTO<>(result, fn);
  }
}


///////////////////////////////////////////////////////////


// Repository - Board
// SearchBoardRepository [ QuerydslSupport Interface 상속 추가 ]
public interface BoardRepository extends JpaRepository<Board,Long> , SearchBoardRepository {
  // ✅중요 : 실제 사용 Method는 SearchBoardRepository(Impl)에서 구현한다.
}


///////////////////////////////////////////////////////////


// QuerySupport - Interface
public interface SearchBoardRepository {
    // 파라미터로 PageRequestDTO를 사용하지 않는 이유는
    // Repository에서 DTO의 사용의도를 생각해보면 쉽다
    // DTO(Data Transfer Object)란
    // 말 그대로 계층간 데이터 교환을 위해 사용하는
    // 객체(Java Beans) 이기에 취지에 적합하지 않기 때문이다.
    Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable);
}


///////////////////////////////////////////////////////////


// QurySuppor - Impl
/**
*  1 . QuerydslRepositorySupport를 상속 받는다.
 * 2. 부모 Class 인 QuerydslRepositorySupport 에 생성자가 요구하는 값을 추가해준다.
 *    -> Class를 요구하는데 내가 원하는 <strong> 데이터 주체의 Entity Class 이다!</strong>
 * 3. 구현하고자 하는 interface를 impl 시킨 후 구현해준다.
* */
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    //생성자 추가 [필수]
    public SearchBoardRepositoryImpl() {
        //내가 사용할 Entity Class
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable) {

        log.info("search With Sort");

        //1. Q도메인 객체 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JQPLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);

        //3. Join 추가
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //4. Generic을 Tuple로 변경
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        //3. 인자로 받아온 조건 추가 - BooleanBuilder, BooleanExpression
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(board.bno.gt(0L)); // bno가 0보다 크고

        //4. type 체크 알맞은 조건 추가
        if(type != null){
            if(type.contains("t")) booleanBuilder.or(board.title.contains(keyword));
            if(type.contains("w")) booleanBuilder.or(member.email.contains(keyword));
            if(type.contains("c")) booleanBuilder.or(board.content.contains(keyword));
        }//if

        //5. 3~4번에서 만들어준 조건을 typle에 추가
        tuple.where(booleanBuilder);

        //////////////////////////////////////
        // - 정렬 추가 -  //
        //tuple.orderBy(pageable.getSort()); ❌  Parameter가 다르므로 사용 불가능 !!

        // ✅ 해결 방법
        // Pageable에서 받아온 sort 을 사용하여 OrderSpecifier 객체를 만들어 추가해준다.

        Sort sort = pageable.getSort();
        //정렬 조건이 여러개일수 있기에 forEach 사용
        sort.stream().forEach( order->{
            // i. 정렬 조건 값을 가져옴
            String prop = order.getProperty();
            // ii. parable에서 정렬 방식 확인 후 Order 객체 생성
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            // iii. PathBuilder 객체 생성 - [ 🎈주의 : Generic으로는 Entity Class를 사용해야한다! ]
            PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class,"board");
            // iv. orderBy()에 맞는 Parameter 사용
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });

        //////////////////////////////////////

        //6. count를 사용했으니 board에 groupBy 시켜줌
        tuple.groupBy(board);

        //7. Paging을 위한 offset값 과 limit 추가
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());


        //8. 타입을 List<Tuple>로 변경
        List<Tuple> result = tuple.fetch();
        log.info("result :: {}", result);

        //9. count 변수 추가 및 값 주입 [Paging을 위함] - 해당값을 구핼때 따로 count를
        //   구하는 Query가 돌아감!
        long count = tuple.fetchCount();
        log.info("count :: {}",count);

        //10.반환 타입은 PageImpl<Object[]> 형식으로 반환하며
        //   위에서 얻은 fetch 데이터와 pageable, count를 인자값으로 넘겨주자
        //주의 사항 : PageImpl 객체 생성 시 List<Tuple>로 전달하는데
        //          이것을 Array로 변환해서 넘겨줘야한다!
        //          그래야 한곳에서 조작해서 넘기기에 다음 단계에서 또다시 변환 해줄
        //          필요가 없어서 편함!
        return new PageImpl(result.stream().map(Tuple::toArray).collect(Collectors.toList()),pageable,count);
    }
}


```