﻿# MappedSuperClass , Querydsl, Paging

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

```properties
# ℹ️ 설정 방법에는 2가지 방법이 있다.
#   1) JPAQueryFactory를 사용 하는 방법 
#   2) QuerydslRepositorySupport를 사용하는 방법
```

#### 1) JPAQueryFactory 이용 방법


##### QueryDSLConfig 설정 Class

```java
@Configuration
@RequiredArgsConstructor
public class QueryDSLConfig {
  private final EntityManager entityManager;

  @Bean
  public JPAQueryFactory jpaQueryFactory(){
    return new JPAQueryFactory(entityManager);
  }
}
```

##### Repository 

- Querydsl **전용 Interface를 추가** 해줘야한다
  - 해당 Interface를 구현한 구현채 Clsss 구현 필요
    - 구현 Class 내 `JPAQueryFactory`를 **의존성 주입 후** 해당 객체의 메서드를 통해 Qureydls 사용
- 기존 Repository Interface에 **Querydsl 전용 Interface를 상속** 하여 사용 

###### Query dsl 사용을 위한 Support Interface
```java
public interface MemberSupport {
  List<Member> getAllList();
}
```
###### Support Interface 구현 Class
- `JPAQueryFactory` 의존성 주입
```java
@RequiredArgsConstructor
public class MemberSupportImpl implements MemberSupport {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Member> getAllList() {
    QMember qMember = QMember.member;
    return jpaQueryFactory.from(qMember)
            .select(qMember)
            .fetch();
  }
}
```

###### Interface 상속
```java
public interface MemberRepository extends JpaRepository<Member, String>, MemberSupport {
}
```


#### 2) QuerydslRepositorySupport 이용 방법

##### Repository
- Querydsl **전용 Interface를 추가** 해줘야한다
  - 해당 Interface를 구현한 구현채 Clsss 구현 필요
    - 구현 Class 내 `QuerydslRepositorySupport`를 상속 받아 Query Dsl 사용
- 기존 Repository Interface에 **Querydsl 전용 Interface를 상속** 하여 사용

##### Support Interface
```java
public interface SearchBoardRepository {
    Board search1();
}
```
##### Support Interface 구현 class 
- `QuerydslRepositorySupport`를 상속
- 생성자 Method를 통해 **부모 생성자에 Entity Class 전달**
- Support Interface를 구현

```java
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

###### Interface 상속
```java
public interface BoardRepository extends JpaRepository<Board,Long> , SearchBoardRepository {
}
```

## 3 ) 페이징 처리 - findAll() 활용

### Page Request DTO
- 페이징 처리에 사용 될 Request DTO
  - 기본 생성자 메서드를 등록 구현하기에 `@NoArgsConstructor` 제외
    - 값이 없을 경우 default 값 지정 위함
```java
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

### Page Response DTO
- Generic으로 DTO, Entity를 받음
  - **확장성**을 위해서임
    - DTO의 경우 **반환 값**
    - Entity의 경우 `Funtion`을 전달받아 **형태 변환**을 할 경우 필요
```java
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

### Service
```java
//java - Servcie Inferface

public interface GuestbookService {
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);
}
```

### ServiceImpl

```java
@Service
@RequiredArgsConstructor
@Log4j2
public class GuestbookServiceImpl implements GuestbookService{

    // 기본 페이징 조회
    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        //1 . Pageable 객체 생성 : PageRequestDTO 내 getPageable() 사용
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

## 4 ) @ModelAttribute 란❔
- 전달 받은 Parameter를 그대로 View에 전달 할 수 있다.
  - 객체형태로 매핑되어 Model을 따로 만들어서 전달할 필요가 없음
  - Default 생성자가 있을 시 해당 데이터로 전달
- `RedirectAttributes`를 사용하면 해당 값을 갖은채로 페이지 이동이 가능함

### Controller

```java
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

@PostMapping("/direct")
public String modelAttrWithRedirect(@ModelAttribute("modelTestData") PageRequestDTO dto
            , RedirectAttributes redirectAttributes){
    redirectAttributes.addFlashAttribute("page"    , dto.getPage());
    redirectAttributes.addFlashAttribute("keyword" , dto.getKeyword());
    redirectAttributes.addFlashAttribute("type"    , dto.getType());
    return "redirect:/paging/view";
}
```


## 5 ) QuerydDsl Join 

- Select의 주체가 되는 Entity가 아닐 경우 Tuple 형태로 반화 된다.
  - ex) `List<Tuple>`
  - 해당 Tuple 객체는 Object[] 형태로 fetch 되기에 배열 형태로 값을 가져가 써야함
    - Expression 기반 접근 방식으로 접근이 가능하다
      ```java
      // String name = tuples.get(0).get(qMember.name);
      // Integer age = tuples.get(0).get(qMember.age);

      // 또다른 방법 - 🛑 안정적인 방법이 아님!
      // Long tupleProjectId     = item.get(0 , Long.class);
      // Integer tupleDoiLevel   = item.get(2 , Integer.class);
      ```
      
```java
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    @Override
    public Board search2WithJoinAddTuple() {

        //1 . Q도메인 생성
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        //2. JPQLQuery 객체 생성
        JPQLQuery<Board> jpqlQuery = from(board);
        //3. Join
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //4. Tuple 객체로 데이터를 받아옴
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member,  reply.count()).groupBy(board);
        List<Tuple> result = tuple.fetch();

        //<Tuple>을 사용 시
        // result.get(0).get(QBoard) 이런식으로 접근이 가능해짐! 👍
        log.info("result :: {}", result);
        return null;
    }
}
```

## 6 ) QuerydDsl - 페이징 처리

### 6 - 1 ) JPQLQuery - booleanBuilder 
- `booleanBuilder`를 사용해서 Where 절 처리가 가능하다

```java
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

    //1. Q도메인 객체 생성
    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    //2. JQPLQuery 객체 생성
    JPQLQuery<Board> jpqlQuery = from(board);

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
```

### 6 - 2 ) JPQLQuery - Tuple Sort 처리
```properties
# ℹ️ List<Tuple> 객체 내에서는 Sort객체를 사용 할 수 없다
#    ㄴ 이유 : 지원을 하지 않기에
#    ㄴ> 해결방법 : OrderSpecifier 객체를 만들어서 진행하면 된다.
```
```java
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements   SearchBoardRepository {
  @Override
  public Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable) {

      //5. 3~4번에서 만들어준 조건을 Tuple에 추가
      tuple.where(booleanBuilder);

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

      //6. count를 사용했으니 board에 groupBy 시켜줌
      tuple.groupBy(board);

      //7. Paging을 위한 offset값 과 limit 추가
      tuple.offset(pageable.getOffset());
      tuple.limit(pageable.getPageSize());

      //8. 타입을 List<Tuple>로 변경
      List<Tuple> result = tuple.fetch();

      //9. count 변수 추가 및 값 주입 [Paging을 위함] - 해당값을 구핼때 따로 count를
      //   구하는 Query가 돌아감!
      long count = tuple.fetchCount();

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

### 6 - 3 ) 전체 적용 코드

```java
@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    public SearchBoardRepositoryImpl() { super(Board.class); }

    @Override
    public Page<Object[]> searchPageWithSort(String type, String keyword, Pageable pageable) {

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

        //6. count를 사용했으니 board에 groupBy 시켜줌
        tuple.groupBy(board);

        //7. Paging을 위한 offset값 과 limit 추가
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());


        //8. 타입을 List<Tuple>로 변경
        List<Tuple> result = tuple.fetch();

        //9. count 변수 추가 및 값 주입 [Paging을 위함] - 해당값을 구핼때 따로 count를
        //   구하는 Query가 돌아감!
        long count = tuple.fetchCount();

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
