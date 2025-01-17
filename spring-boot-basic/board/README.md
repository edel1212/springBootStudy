# N:1, FetchType, JQPL 페이징


## 1 ) @ManyToOne 연관관계 ❔

- 연관 관계를 설정 할 경우 **FK를 기준으로 설계**하면 **더 쉽게 설계**가 가능
  - 예시
    - Board(게시글) 와 Memeber(회원)의 관계는 **N : 1 의 관계**가 되므로 JPA에서 이를 의미하는 `@ManyToOne`을 적용할 수 있다.
      - 여러개의 게시글( N - Boarder)는 한명의 작성자( 1 - Member)가 작성함
- 헷갈리는 개념
  - @ManyToOne 과 @OneToMany 
    - `@ManyToOne`
      -  다수(많은 쪽)의  Entity에서 해당 어노테이션을 FK 대상에 적용
        - 대상 **FK의 타입은 Class** 이다
    - `@OneToMany`
      - 적은 쪽의 Entity에서 해당 어노테이션을 FK 대상에 적용
        - 대상 **FK의 타입은 List** 이다
\- 연관관계 Entity 생성🔽

### Entity 구조

#### @ManyToOne Entity

```java
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Board  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne
    @ToString.Exclude     
    private Member writer; 

}

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne
    @ToString.Exclude    
    private Board board; 

}
```


### FetchType
```properties
# ℹ️ 연관관계 데이터를 어떻게 가져올 것인가에 대한 용어를 "fetch"라고 하며, 연관관계의 어노테이션 속성으로 "fetch mode" 지정이 가능
#   ㄴ> @ManyToOne (fetch = FetchType.???)
```

#### "Eager loading"(즉시 로딩)
- 아래의 테스트 코드를 진행하면 비효율적으로 모든 `board`, `member`정보까지 모두**join되는 것**을 볼 수 있음
  - 위와 같은 특정한 엔티티를 조회할 때 **연관관계를 가진 모든 엔티티**를 같이  조회하는 것을 **"Eager loading"(즉시 로딩)** 이라 함
    - JPA **Default로 설정** 되어 있음
    - 한번에 연관된 모든 정보를 가져오기에 편리 하지만 **연관 관계 복잡해 질수록** 불필요한 Join이 늘어나 **성능 저하 이슈**가 있음  

#### 테스트 코드
```java
class test{
  @Test
  public void restTest1(){
      Optional<Reply> result = replyRepository.findById(1L);
      result.ifPresent(log::info);
  }
}
```

#### Result Query
```javascript
/*   
 *     select
 *         reply0_.rno as rno1_2_0_,
 *         reply0_.board_bno as board_bn4_2_0_,
 *         reply0_.replyer as replyer2_2_0_,
 *         reply0_.text as text3_2_0_,
 *         board1_.bno as bno1_0_1_,
 *         board1_.moddate as moddate2_0_1_,
 *         board1_.regdate as regdate3_0_1_,
 *         board1_.content as content4_0_1_,
 *         board1_.title as title5_0_1_,
 *         board1_.writer_email as writer_e6_0_1_,
 *         member2_.email as email1_1_2_,
 *         member2_.moddate as moddate2_1_2_,
 *         member2_.regdate as regdate3_1_2_,
 *         member2_.name as name4_1_2_,
 *         member2_.password as password5_1_2_
 *     from
 *         reply reply0_
 *     left outer join
 *         board board1_
 *             on reply0_.board_bno=board1_.bno
 *     left outer join
 *         member member2_
 *             on board1_.writer_email=member2_.email
 *     where
 *         reply0_.rno=?
 */
```

#### "Lazy loading"(지연 로딩)
- 원하는 데이터만 조회를 원한 경우 `@ManyToOne (fetch = FetchType.LAZY)` 추가 해주자
  - 지연 로딩은 조인을 하지 않기 때문에 단순하게 하나의 테이블을 이용 하는 경우 빠른 처리가 가능함
-  🤬 주의사항 : 연관관계가 있는 Entity Class에서 ToString()을 사용을 주의하자
  -  조회 시 해당 클래스의 모든 멤버 변수를 출력하게 되어 Board(wrtier) -> Memeber 객체 호출하기 떄문
    - **전파된다** 생각하면 이해하기 쉬움
  - 해결방안 👍
    - 연관관계 변수는 @ToString.Exclude를 적용하여 제외 시켜주자

#### Entity Class
```java
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Board  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;

    @ManyToOne (fetch = FetchType.LAZY) // ✔ 명시적으로  Lazy Loading 지정!
    @ToString.Exclude                   // 해당 변수는 ToString 에서 제외 시킴!
    private Member writer;              // 연관관계 지정
}
```

#### 테스트 코드
```java
class Contoller{
  @GetMapping("/reply")
  ResponseEntity<String> getRepy(){
    // Reply만 조회
    Reply reply =  replyRepository.findById(1L)
            .orElseThrow();
    // Board 쿼리를 함
    log.info(reply.getBoard());
    // 주석을 풀 경우 Member 쿼리
    //log.info(reply.getBoard().getWriter());
    return ResponseEntity.ok("success");
  }
}
```

#### Result Query
```javascript
/**
    select
        r1_0.rno,
        r1_0.board_bno,
        r1_0.replyer,
        r1_0.text 
    from
        reply r1_0 
    where
        r1_0.rno=1
2025-01-01T20:25:26.142+09:00  INFO 39621 --- [nio-8080-exec-1] p6spy                                    : commit 16ms
2025-01-01T20:25:26.201+09:00  INFO 39621 --- [nio-8080-exec-1] p6spy                                    : statement 52ms 
    select
        b1_0.bno,
        b1_0.content,
        b1_0.title,
        b1_0.writer_id 
    from
        board b1_0 
    where
        b1_0.bno=1
*/
```

## 3 ) JPQL을 사용한 - left (outer) join

###  Entity Class 간 연관관계가 "있을" 경우
- 일반적인 Query문의 JOIN문을 생각하면 쉽다.
  - JOIN 시 조건을 작성 할 때 ON 절을 사용 해당 경우와 같이 연관관계가 이어져 있다면 **이미 연관이** 있는 상태이므로 **ON을 제외** 가능
#### Repository
```java
public interface BoardRepository{
  /**
   * - Result Query
   *    Hibernate:
   *     select
   *         board0_.bno as bno1_0_0_,
   *         member1_.email as email1_1_1_,
   *         board0_.moddate as moddate2_0_0_,
   *         board0_.regdate as regdate3_0_0_,
   *         board0_.content as content4_0_0_,
   *         board0_.title as title5_0_0_,
   *         board0_.writer_email as writer_e6_0_0_,
   *         member1_.moddate as moddate2_1_1_,
   *         member1_.regdate as regdate3_1_1_,
   *         member1_.name as name4_1_1_,
   *         member1_.password as password5_1_1_
   *     from
   *         board board0_
   *     left outer join
   *         member member1_
   *             on board0_.writer_email=member1_.email    👍자동으로 on 조건이 걸려있음!👍
   *     where
   *         board0_.bno=?
   *
   * **/
  @Query("SELECT b, w FROM Board b LEFT JOIN b.writer w WHERE b.bno =:bno") //JPQL 처리
  Object getBoardWithWriter(@Param("bno") Long bno);    
}
```

#### Test Code
- Join을 할 경우 응답 값은 **Object**형식으로 반환된다.
  - 각각의 배열에는 조회에 선언한 **필드**가 **배열 값**에 들어가 있음
```java
class Test{
  @Test
  public void testReadWithWriter(){
    Object result = boardRepository.getBoardWithWriter(100L);
    //배열 형식으로 파싱해줘야 사용이 가능하다!
    Object[] arr = (Object[]) result;

    log.info("------------------------");
    // JPQL의 장점인 Object 타입으로 반환 받기에
    // 배열 [0] : Board , [1] Member 형태로 나옴!
    // [ Board(bno=100, title=Title..100, content=Content...100)
    //   , Member(email=user100@naver.com, password=111, name=Yoo100) ]
    log.info(Arrays.toString(arr));
  }
}
```

### Entity Class 간 연관관계가 "없을" 경우
- 연관관계가 없으니 JOIN **"ON"**을 사용해 연관 관계를 만들어 줘야함

#### Repository
- Reply는 `@ManyToOne`으로 Board와 연관관계를 맺고있지만 **Board를 기준**으로 Reply를 바라볼 때 **연관 되어있지 않음**
  - **"ON"**을 사용하여 JOIN 조건을 추가 필요

```java
public interface BoardRepository{
  /**
   *   Hibernate:
   *   select
   *       board0_.bno as bno1_0_0_,
   *       reply1_.rno as rno1_2_1_,
   *       board0_.mod_date as mod_date2_0_0_,
   *       board0_.reg_date as reg_date3_0_0_,
   *       board0_.content as content4_0_0_,
   *       board0_.title as title5_0_0_,
   *       board0_.writer_email as writer_e6_0_0_,
   *       reply1_.mod_date as mod_date2_2_1_,
   *       reply1_.reg_date as reg_date3_2_1_,
   *       reply1_.board_bno as board_bn6_2_1_,
   *       reply1_.replyer as replyer4_2_1_,
   *       reply1_.text as text5_2_1_
   *   from
   *       board board0_
   *   left outer join
   *       reply reply1_
   *           on (
   *               reply1_.board_bno=board0_.bno
   *           )
   *   where
   *       board0_.bno=?
   * */
  @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
  List<Object[]> getBoardWithReply(@Param("bno") Long bno); //다건이므로 List<Object[]> 타입이다.    
}
```

## 4 ) JPQL을 사용한 - Paging

### Repository

#### 불필요한 Left Join
- 페이징에 필요한 총 개수(CountQuery)를 구하는 쿼리에 불필요한 Join이 들어가 있는 것을 볼 수 있음
```java
public interface BoardRepository extends JpaReposoitry<Board, Long>{
  /**
   *  ℹ️ 문제가 없는 SELECT b, w, count(r) 부분의 Qurey log는 제외함
   *  
   *    select
   *        count(board0_.bno) as col_0_0_
   *    from
   *        board board0_
   *    left outer join
   *        member member1_
   *            on board0_.writer_email=member1_.email
   *    left outer join
   *        reply reply2_
   *            on (
   *                reply2_.board_bno=board0_.bno
   *            )
   *    group by
   *        board0_.bno
   * */
  @Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w" +
          " LEFT  JOIN Reply r ON r.board = b" +
          " GROUP BY b")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);    
}
```

#### countQuery를 사용한 불필요한 Join을 제거 👍
- countQuery
  - Page 반환 타입일경우 페이징 수를 계산할 총 해당 Table의 총 개수가 필요
    - 따로 질의 후 추가해주는 방법 또는 JPQL에서의 자동으로 추가되어 적용할 수 있음 
- 사용 방법
  - `@Query("질의", countQuery = "SELECT COUNT(b) FROM Board b" )`를 사용  Paging에 필요한 count Qeruy를 작성
```java
public interface BoardRepository extends JpaReposoitry<Board, Long>{
  /**
   *   select
   *       count(board0_.bno) as col_0_0_
   *   from
   *       board board0_
   */
  @Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w" +
          " LEFT JOIN Reply r ON r.board = b" +
          " GROUP BY b" //w 의 경우 b에 포함되어있기 때문에 Group By 포함 하지 않아도된다.
          , countQuery = "SELECT COUNT(b) FROM Board b")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);    
}
```

<hr/>

## 5 ) Object[] -> DTO Mapping 

### DTO Class
```java

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    //기존 Board Entity 다른 추가사항 -- Member 객체에서 가져온 데이터를 넣을 변수
    private String writerEmail;
    private String writerName;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    //기존 Board Entity
    //다른 추가사항 -- 연관관계가 없지만 JOIN을 이용해 가져온 데이터를 넣을 변수
    private int replyCount; //해당 게시글의 댓글 수
}
```

### Business Logic

#### Service

```java
public interface BoardService {

  //페이징 목록
  PageResultDTO<BoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);
  
  default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
    return BoardDTO.builder()
            .bno(board.getBno())
            .title(board.getTitle())
            .content(board.getContent())
            .regDate(board.getRegeDate())
            .modDate(board.getModDate())
            .writerEmail(member.getEmail())     // Member 객체에서 받아옴
            .writerName(member.getName())       // Member 객체에서 받아옴
            .replyCount(replyCount.intValue())
            .build();
  }
}
```

#### ServiceImpl
- Object[]의 **Index 순서**는 질의하려는 **필드 값의 순서대로** 들어가 있다

```java
@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  //Board Repository 주입
  private final BoardRepository repository;

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

      Function<Object[],BoardDTO> fn =
                ( en-> this.entityToDTO( (Board)en[0], (Member)en[1], (Long)en[2]) );

      Pageable pageable     = requestDTO.getPageable(Sort.by("bno").descending());
      Page<Object[]> result = boardRepository.getBoardWithReplyCount( pageable );

      //PageReultDTO 객체 생성
      return new PageResultDTO<>(result,fn);
  }
}
```


## 6 ) 연관 관계가 있을 경우의 삭제
- JPQL 사용 `update, delete`를 진행 시 `@Modifying` 어노테이션은 **필수**다.
  -  QueryMethod는 제외 가능 
- 참조 되고있는 Table[FK]이 있을 경우 해당 Table의 내용도 함께 삭제 필요
  - 트랜젝션 처리가 필요함

### Repository
- 지정 게시글(Board) 내 모든 댓글(Reply) 삭제
```java
public interface ReplyRepository extends JpaRepository<Reply,Long> {
  @Modifying 
  @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
  void deleteByBno(Long bno);
}
```

### ServiceImpl
```java
@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  //Board Repository 주입
  private final BoardRepository repository;
  //Reply Repository 주입
  private final ReplyRepository replyRepository;

  @Transactional //필수
  @Override
  public void removeWithReplies(Long bno) {
    /**
     * 댓글을 먼저 삭제 해준다  - 이유❔ ::  Reply Entity[FK] 이기 때문
     * - 중요 : 해당 deletByBno의 경우 @Modifying 어노테이션이 추가되어있음
     */
    replyRepository.deleteByBno(bno);

    // ✅ Reply 삭제 후 Board 삭제
    /***
     * - 상당 Reply의 경우 JPQL을 사용했기에 [해당 Repository에]@Modifying를 추가해줘야했지만
     * - 아래와 같은 경우 Jpa 내장 Method를 사용하므로 추가해줄 필요가 없었다!
     */
    repository.deleteById(bno);

  }

}
```

