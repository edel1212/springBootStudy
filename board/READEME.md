<h1>N:1(다 : 1) 연관관계, querydsl(JPQLQuery 객체)</h1>

<h3>1 ) @ManyToOne 연관관계 ❔</h3>

- JPA에서 관계를 설계할 때는 FK 쪽을 기준으로 해석해보면 좀 더 쉽게 설계가 쉽다.
  <br/> 현재 예제에서는 Board 와 Memeber를 사용할 것인데 둘의 관계는 N : 1 (다대일)의 관계가
  <br/> 되므로 JPA에서 이를 의미하는 @ManyToOne을 적용해야한다.
- @ManyToOne은 DB상에서 <b>외래키를 관계로 연결되 Class를 설정해준다
  <br/>ㄴ> [Board의 Member인 변수 , Reply의 Board인 변수 ]
- 여기서 헷갈리는 개념이 ManyToOne 과 OneToMany 인데
  <br>ㄴ> 쉽게 설명해서 내가 많고 대상이 한개인 개념에 MayToOne을 사용하면된다. [ FK가 생설될 곳 ]

\- 연관관계 Entity 생성🔽

```java
// java - Entity Class

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
    @ToString.Exclude      // 해당 변수는 ToString 에서 제외 시킴!
    private Member writer; //연관관계 지정 -- > 자동으로 커럼명은 writer_email 로 됨 변수명_PK 로 된다

}


///////////////////////////////////////////////////////////////////////////////////


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    private String email;

    private String password;

    private String name;

}


///////////////////////////////////////////////////////////////////////////////////


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
    @ToString.Exclude    //ToString 에서 제외시킴 - 이유는 해당 변수를 호출하기 위해서는 데이타베이스에 연결이 필요하기 때문임
    private Board board; //연관관계 지정 -- > 자동으로 커럼명은 board_bno 로 됨 변수명_PK 로 된다

}
```

\- 연관관계 Entity Insert🔽

```java
//java - JUnit Test Code

//Memeber의 경우 연관된 것이 없으므로 그냥 Insert
@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
  IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("user"+i+"@naver.com")
                    .password("111")
                    .name("Yoo"+i)
                    .build();
            memberRepository.save(member);
    });
}


/////////////////////////////////////////////////////


//Board의 경우 Member와 1:N 관계이므로 객체를 생성해서 넣어줘야한다.
@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
  IntStream.rangeClosed(1,100).forEach(i->{
        // Member ID가 존재하는 Member 객체변수를 만들어야한다.
        Member member = Member.builder().email("user"+i+"@naver.com").build();

        Board board = Board.builder()
                .title("Title.."+i)
                .content("Content..."+i)
                .writer(member) //상단에서 만들어준 Member객체를 넣어줌
                .build();

        boardRepository.save(board);//완성된 Board 객체변수를 argument로 넣어줌

    });
}


/////////////////////////////////////////////////////


//Reply의 경우 Board와 1:N 관계 위와 같음
@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
  @Test
  public void insertReply(){
    IntStream.rangeClosed(1,300).forEach(i->{
        //Board의 존재하는 bno [ 1 ~ 100]으로 만들었음
        long bno = (long) ( Math.random() * 100 + 1 );

        //Board 객체 생성
        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder().text("ReplyText..."+i)
                .board(board) // 상단에서 만든 객체변수 주입
                .replyer("guest"+i)
                .build();

        replyRepository.save(reply);
  }
}
```

\- 연관관계 Entity Select🔽
<br/> ❌ Member의 경우 조회 시 <b>Join 되는 것이 없으니 테스트에서 제외함</b>

```java
//java - JUnit Test Code

// Board "100L" Select
@Test
public void readTest1(){
    Optional<Board> result = boardRepository.findById(100L);
    result.ifPresent(log::info);
}
/*
 * - Result Query
 *  Hibernate:
 *   select
 *       board0_.bno as bno1_0_0_,
 *       board0_.mod_date as mod_date2_0_0_,
 *       board0_.reg_date as reg_date3_0_0_,
 *       board0_.content as content4_0_0_,
 *       board0_.title as title5_0_0_,
 *       board0_.writer_email as writer_e6_0_0_,
 *       member1_.email as email1_1_1_,
 *       member1_.mod_date as mod_date2_1_1_,
 *       member1_.reg_date as reg_date3_1_1_,
 *       member1_.name as name4_1_1_,
 *       member1_.password as password5_1_1_
 *   from
 *       board board0_
 *   left outer join
 *       member member1_
 *           on board0_.writer_email=member1_.email
 *   where
 *       board0_.bno=?
 *  =============================================================================
 *  내부적으로 member 테이블이 left outer join 되어 조회 되는것을 확인 할 수있음.
 *  - Board는 Member와 @ManyToOne 관계이기 때문임
 * */



//////////////////////////////////////////////////////////


// Reply "1L" Select  🤬문제 발생 - 비효율적 쿼리 발생.
@Test
public void restTest1(){
    Optional<Reply> result = replyRepository.findById(1L);
    result.ifPresent(log::info);
}
/*
*  - Result Query
*    Hibernate:
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
*
*         ---------------------------------------------------------------
*
*     ✔ 위와같이 불필요하게 reply, board, member 까지 전부 left outer join처리 되어있는것을
*        확인 할수 있는데 굉장히 비효율적이다
*        => 예상했던것은 Board만 left outer join이었음
*
*     ✔ 위와 같은 특정한 엔티티를 조회할 때 연관관계를 가진 모든 엔티티를 같이
*        로딩하는 것을 [ ✔중요 : "Eager loading"(즉시 로딩) ] 이라고 한다
*
*     ✔ 즉시 로딩은 한번에 연관관계가 있는 모든 엔티티를 가져온다는
*        장점이 있지만 여러관계를 맺고 있거나 연관관계가 복잡할수록 불필요한 조인이 늘어나
*        성능 저하 이슈가 있음
*
*     ✔ JPA 에서 연관관계 데이터를 어떻게 가져올 것인가를 [ ✔중요 : fetch(패치) ]라고 하며
*       연관관계의 어노테이션 속성으로 "fetch 모드" 지정이 가능하다
*       => ex) @ManyToOne (fetch = FetchType.???)
*
*     🎈 위와 같은 Eager loading(즉시 로딩)은 불필요한 조인까지 처리해야하는 경우가
*        많기 때문에 가능한 사용하지 않고 그와 반대 개념으로
*        [ ✔중요 : "지연로딩"(Lazy loading) ] 으로 처리해 주자
*        -  Board Class 수정이 필요 @ManyToOne (fetch = FetchType.LAZY) 추가
*        -  지연로딩처리를 한 class 의 정보를 가져오는 Service 에는
*           @transaction 처리가 필수이다!
*          ❌ 없을경우 no-session Error [ @see : BoardRepositoryTests.java ]
*          --> 해당 오류의 이유는 board.getWriter()는 member 테이블을 로딩해야 하는데
*              이미 데이터베이스와의 연결이 끝난 상태이기 때문이다. --- Lazy loading!!
*
*          --> 이러한 문제를 해결하기 위해서는 다시 한번 데이터베이스와 연결이 필요한데
*              이럴때 @Transactional으로 해결이 가능하다.
*
* **/

```

\- FetchType.Lazy 추가 Board Entity Class🔽
<br/> 🤬 주의사항 : 연관관계가 있는 Entity Class에서 ToString()을 사용시 주의를 해야한다.
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 그 이유는 해당 클래스의 모든 멤버 변수를 출력하게 되어 Board(wrtier) -> Memeber 객체 호출
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -> Member에서도 ToString() 호출 하는 문제가 발생한다.
<br/>&nbsp; 👍해결방안 : 연관관계 변수는 @ToString.Exclude를 적용해주자

```java
//java - Entity Class

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

    //@ManyToOne  // N : 1 관계임!          ✔ 그냥 사용시 Eager loading 이 Default 값임
    @ManyToOne (fetch = FetchType.LAZY) // ✔ 명시적으로  Lazy Loading 지정!
    @ToString.Exclude                   // 해당 변수는 ToString 에서 제외 시킴!
    private Member writer;              // 연관관계 지정
                                        //-- > 자동으로 커럼명은 writer_email 로 됨 변수명_PK 로 된다

}
```

\- FetchType.Lazy 추가 후 Select Test🔽

```java
// JUnit Test Code - Select

//- - Reply Select  - -
@Test
public void restTest1(){
    Optional<Reply> result = replyRepository.findById(1L);
    result.ifPresent(log::info);
}
/* 아래 보이는 쿼리처럼 불필요한 Member Table을 Join 하지 않음!
*  - Result Query
*  Hibernate:
*      select
*          reply0_.rno as rno1_2_0_,
*          reply0_.mod_date as mod_date2_2_0_,
*          reply0_.reg_date as reg_date3_2_0_,
*          reply0_.board_bno as board_bn6_2_0_,
*          reply0_.replyer as replyer4_2_0_,
*          reply0_.text as text5_2_0_,
*          board1_.bno as bno1_0_1_,
*          board1_.mod_date as mod_date2_0_1_,
*          board1_.reg_date as reg_date3_0_1_,
*          board1_.content as content4_0_1_,
*          board1_.title as title5_0_1_,
*          board1_.writer_email as writer_e6_0_1_
*      from
*          reply reply0_
*      left outer join
*          board board1_
*              on reply0_.board_bno=board1_.bno
*      where
*          reply0_.rno=?
*/


//////////////////////////////////////////////////////////////

//- - Board Select  - -

//🤬문제발생🤬
@Test
public void readTest1(){
    Optional<Board> result = boardRepository.findById(100L);
    if(result.isPresent()){
        log.info(result.get());
        log.info(result.get().getWriter()); // Error 발생
    }
}
// 이슈 사항  : FetchType을 Lazy 로 변경하여 Reply Select 시 불필요한
//             Member Table의 조인을 하지 않게 되었지만 지연로딩을 지정한
//             Board Table을 Select만 할경우에는 문제가 없지만
//             Memeber객체에 데이터를 가져오려 할경우 Error가 발생
//
// Error Msg : could not initialize proxy  - no Session


//🎈해결방안🎈
// 이유     :  FetchType.Lazy(지연로딩)의 경우 지연로딩 방식으로 로딩하기 떄문에
//            Board Table만 가져오는 쿼리를 날리지만 위의 테스트처럼 Member객체의
//            writer에 접근하려하면 이미 데이터베이스와의 연결이 끝난 상태이기 떄문이다
//            이러한 경우에 나오는 Eror가 no Session Error 이다.
//
// 해결방법 : 데이터베이스의 연결이 끊어 졌다면 다시 한번 더 데이터베이스와 연결하면된다.
//           @Transctional 어노테이션을 사용하면 다시 한번 더 필요한 데이터를
//           가져올 때 데이터베이스에 연결이 가능하다.
//           - @Transctional은 해당 메서드를 하나의 트랜잭션으로 처리한다는 의미이다.
//           - 트랜잭션으로 처리하면 속성에 따라 다르게 동작하지만 기본적으로 필요한 데이터를
//             가져올경우 다시 데이터베이스와 연결이 생성된다.
@Test
@Transactional  // no Session Error를 막음 - 다시 한번 더 DB와 연결함
public void readTest1(){
    Optional<Board> result = boardRepository.findById(100L);
    //result.ifPresent(log::info);

    if(result.isPresent()){
        log.info(result.get());
        log.info(result.get().getWriter()); // Transactional 이 없을시 Error
    }

}

/* 아래 보이는 쿼리처럼 Member의 writer 접근시 한번 더 쿼리가 생성되는것을
*    확인할 수 있다.
*  - Result Query
* Hibernate:
*    select
*        board0_.bno as bno1_0_0_,
*        board0_.mod_date as mod_date2_0_0_,
*        board0_.reg_date as reg_date3_0_0_,
*        board0_.content as content4_0_0_,
*        board0_.title as title5_0_0_,
*        board0_.writer_email as writer_e6_0_0_
*    from
*        board board0_
*    where
*        board0_.bno=?
* ... Log ..
* Hibernate:
*    select
*        member0_.email as email1_1_0_,
*        member0_.mod_date as mod_date2_1_0_,
*        member0_.reg_date as reg_date3_1_0_,
*        member0_.name as name4_1_0_,
*        member0_.password as password5_1_0_
*    from
*        member member0_
*    where
*        member0_.email=?
**/

```

<hr/>

<h3>2 ) 지연로딩(lazy Loading)의 장단점❔</h3>

- 장점🔽
  <br/>지연 로딩은 조인을 하지 않기 때문에 단순하게 하나의 테이블을 이용 하는 경우 빠른 처리가 가능함

- 단점🔽
  <br/>연관관계가 복잡한 경우에는 어려번의 쿼리가 실행된다는 단점이 있음

- 보편적인 방법❔
  <br/>지연로딩을 기본으로 하고 상황에 맞게 필요한 방법에 맞게 사용해야함

<hr/>

<h3>3 ) JPQL 과 left (outer) join</h3>
- 스프링 부트 2버전 이후에 부터는 JPA에서 <b>Entity Class 간의 연관관계가 없더라도 INNER JOIN, JOIN 과 같이 </b>
<br/>  &nbsp;&nbsp;&nbsp;일반적 조인 이 외에도 LEFT(RIGHT) OUTER JOIN 혹은 LEFT(RIGHT) JOIN을 이용할수있다.

<br/>

\- Entity Class 간 연관관계가 있을(👌) 경우🔽
<br/>ㄴ> 🎈간단 설명 : 일반적인 Query문의 JOIN문을 생각하면 쉽다. JOIN 시 조건을 작성 할 때
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ON 절을 사용한다, 해당 경우와 같이 연관관계가 이어져 있다면 연관이 이미 있는 상태이므로
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>ON을 제외가 가능한것이다.👍</b>

```java
//java - JpaRepository

/**
 * @Descripciton  :  해당 쿼리는 JPQL문법으로 쿼리와 다르다 DB로 돌려도 안나옴!
 *
 *                  ✔ 얻고자 하는 데이터는 Board와 Member를 같이 받아오고 싶은것이다.
 *                    Board는 Memeber 와 연관관계를 맺고 있으므로
 *                    ☑ "Board에서 Member writer 변수로 이용 중"
 *
 *                    Query 내에서 변수로 사용가능하다  ex) b.writer w
 *
 *                    🎈중요 1 ) b.writer w 와 같이 변수 형태로 사용한다
 *                           2 ) - 연관 관계가 있을 경우 JOIN 시 ON 이 <b>필요없지만<b/>
 *                                 [현재의 경우임]
 *
 *                               - 연관관계가 없을 경우 JOIN 시 ON 이 <b>필요하다<b/>
 *
 *
 *                  ✔ 해당 쿼리 결과를보면 Board 의 FK 인 Member writer 변수에 Lazy 처리 후
 *                     Member 데이터에 접근하했는데도 @Transactional을 사용하지 않았지만
 *                     Error가 발생하지 않는다!
 *                    - ✅이유 : 아래 Query를 보면 Join을 이용하여 한번에 처리하여 데이터를
 *                               가져왔기 때문에 @Transactional을 사용하지 않아도 에러가
 *                               나지 않는것이다.
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


///////////////////////////////////////////////////////////////////////////

//java - JUnit Test [연관관계가 있을경우 JPQL Join Test]
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

```

\- Entity Class 간 연관관계가 없을(❌) 경우🔽
<br/>ㄴ> 🎈간단 설명 : 연관관계가 없으니 JOIN "ON"을 사용해줘야 하는것이다!

```java
//java - JpaRepository

/**
 * @Descripciton  : Reply는 @ManyToOne으로 Board와 연관관계를 맺고있지만
 *                  Board 쪽에서는 따로 Reply를 사용한 변수가 없으므로
 *                  Board를 기준으로 Query를 사용시 연관관계를 찾을 수 없기에
 *                 "ON"을 사용하여 JOIN 조건을 추가해줘야한다.
 *
 * -Result Query
 *
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


//////////////////////////////////////////////////////


//java - JUnit Test [연관관계가 "없을" 경우 JPQL JOIN ON Test]
@Test
public void testGetBoardWithReply(){
    log.info("with Reply Test ! ::: Use join on!!");

    // 100번의 번호를 갖는 Board + Reply
    List<Object[]> result = boardRepository.getBoardWithReply(100L);

    /*
    * 자료 형태 - > Left Outer Join 이기에 Board가 맞춰서 나오는것!
    * [ [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=55, text=ReplyText...55, replyer=guest55)]
    *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=71, text=ReplyText...71, replyer=guest71)]
    *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=211, text=ReplyText...211, replyer=guest211)]
    *    , [Board(bno=100, title=Title..100, content=Content...100), Reply(rno=248, text=ReplyText...248, replyer=guest248)] ]
    * */
    result.stream()
            .map(Arrays::toString)
            .forEach(log::info);
}
```
