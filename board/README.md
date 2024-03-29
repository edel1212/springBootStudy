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

\- JPQL[ JOIN을 사용한 Paging ]🔽
<br/>ㄴ> 주의사항👿 : Query 작성 시 개행 할 경우 띄어쓰기에 주의하자..

```java
//java - JpaRepository

//🤬문제발생🤬
// 문제 원인❔
//  - CountQuery에 불필요한 JOIN이 들어가있음
//
// CountQuery❔
//  - Page 반환 타입일경우 페이징 수를 계산할 총 해당 Table의 총 개수가 필요하다.
//  - 따로 작성해줄수있고 없을경우 자동으로 추가되어 적용된다 현재의 경우
//    자동으로 작성된 Query를 사용하여 성능상 문제가 발생하고 있다.
@Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w" +
        " LEFT  JOIN Reply r ON r.board = b" +
        " GROUP BY b")
Page<Object[]> getBoardWithReplyCount(Pageable pageable);
/**
 * -Result Query
 * Hibernate:
 *    select
 *       board0_.bno as col_0_0_,
 *       member1_.email as col_1_0_,
 *       count(reply2_.rno) as col_2_0_,
 *       board0_.bno as bno1_0_0_,
 *       member1_.email as email1_1_1_,
 *       board0_.mod_date as mod_date2_0_0_,
 *       board0_.reg_date as reg_date3_0_0_,
 *       board0_.content as content4_0_0_,
 *       board0_.title as title5_0_0_,
 *       board0_.writer_email as writer_e6_0_0_,
 *       member1_.mod_date as mod_date2_1_1_,
 *        member1_.reg_date as reg_date3_1_1_,
 *        member1_.name as name4_1_1_,
 *        member1_.password as password5_1_1_
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
 *    order by
 *        board0_.bno desc limit ?
 * ----------------------------------------------
 *  👿아래 Count쿼리를 보면 불필요한 Join이 들어가있는것을 볼수있다👿
 *  Hibernate:
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


//////////////////////////////////////////////////////////


//🎈해결방안🎈
// - countQuery = "..."를 추가하여 Paging에 필요한 count를 내가 지정해줌
@Query(value = "SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w" +
            " LEFT JOIN Reply r ON r.board = b" +
            " GROUP BY b" //w 의 경우 b에 포함되어있기 때문에 Group By 포함 하지 않아도된다.
            , countQuery = "SELECT COUNT(b) FROM Board b")
Page<Object[]> getBoardWithReplyCount(Pageable pageable);
/**
 * Hibernate:
 *   select
 *       board0_.bno as col_0_0_,
 *       member1_.email as col_1_0_,
 *       count(reply2_.rno) as col_2_0_,
 *       board0_.bno as bno1_0_0_,
 *       member1_.email as email1_1_1_,
 *       board0_.mod_date as mod_date2_0_0_,
 *       board0_.reg_date as reg_date3_0_0_,
 *       board0_.content as content4_0_0_,
 *       board0_.title as title5_0_0_,
 *       board0_.writer_email as writer_e6_0_0_,
 *       member1_.mod_date as mod_date2_1_1_,
 *       member1_.reg_date as reg_date3_1_1_,
 *       member1_.name as name4_1_1_,
 *       member1_.password as password5_1_1_
 *   from
 *       board board0_
 *   left outer join
 *       member member1_
 *           on board0_.writer_email=member1_.email
 *   left outer join
 *       reply reply2_
 *           on (
 *               reply2_.board_bno=board0_.bno
 *           )
 *   group by
 *       board0_.bno
 *   order by
 *       board0_.bno desc limit ?
 * -------------------------------------------------------
 * Hibernate:
 *   select
 *       count(board0_.bno) as col_0_0_
 *   from
 *       board board0_
 */
```

<hr/>

<h3>4 ) Object[]를 DTO로 변경하기 - [ JPQL Pageing 반환 ]</h3>

\- DTO Class🔽

```java
//java - DTO

/**
 * @Descripciton  : DTO를 구성하는 기준은 기본적으로 화면에 전달하는 데이터 이거나
 *                  반대로 화면 쪽에서 전달 되는 데이터를 기준으로 하기 때문에
 *
 *                  ✔ 기존 Entity Class 와 일치하지 않는 경우가 많다
 * */
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

\- Service,Impl Class🔽

```java
//java - Service Interface

public interface BoardService {

  //페이징 목록
  PageResultDTO<BoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);

  // DTO 변환 시
  // 각각해당하는 부분에 데이터를 주입하기위해서
  // 3개의 파라미터가 필요하다[ Board, Member, Long ]
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


///////////////////////////////////////////////////////////


//java - Service Implements

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  //Board Repository 주입
  private final BoardRepository repository;

  @Override
  public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

      log.info(pageRequestDTO);

      // DTO로 변환 함수 인데 여기서 Object[]  Index 순서는
      // 내가 JPQL에 작성한 쿼리의 순서이다 @See : boardRepository.java
      // ex) SELECT b, w, count(r) ...
      Function<Object[],BoardDTO> fn =
                ( en-> this.entityToDTO( (Board)en[0], (Member)en[1], (Long)en[2]) );

      Page<Object[]> result =
                boardRepository.getBoardWithReplyCount(
                        requestDTO.getPageable(Sort.by("bno").descending())
                );

      //PageReultDTO 객체 생성
      return new PageResultDTO<>(result,fn);
  }
}


///////////////////////////////////////////////////////////


//java - JUnit Test Code
@SpringBootTest
@Log4j2
public class BoardServiceTests {

  @Autowired
  private BoardService boardService;

  @Test
  public void getListTest(){
      // new PageRequestDTO() 기본 생성자를 넘기면
      // 생성자 매서드에서 ...of(0,1) 생성자 생성
      PageResultDTO<BoardDTO, Object[]> result = boardService.getList(new PageRequestDTO());

      result.getDtoList().forEach(log::info);
  }
}
```

<hr/>
<br/>

<h3>5 ) 연관관계가 있을 경우의 삭제</h3>
<strong>주의사항👿 </strong>
<br/>

- JPQL을 사용해 update, delete를 할경우 @Modifying 어노테이션은 필수이다. [ QueryMethod는 제외가능 ]
- 참조되고있는 Table[FK]이 있을경우 해당 Table의 내용도 함께 삭제해줘야한다.
- 가장 중요한 것은 연관관계가 있는 것들이 같이 삭제되어야 한다는 것인데 이것들은 한 트랜젝션에서 처리 해야한다.

\- 연관관계 삭제 [Board - Reply]🔽

```java
//java - JpaRepository

//ReplyRepository
public interface ReplyRepository extends JpaRepository<Reply,Long> {
  @Modifying //JPQL을 이용해서 Update, Delete를 실행하기 위해서는 해당 어노테이션이 필요함
  @Query("DELETE FROM Reply r WHERE r.board.bno = :bno")
  void deleteByBno(Long bno);
}


//////////////////////////////////////////////////////////////////////////////////////

//java - Service,Impl

//Board Service - ServiceImple
//삭제 -- ✔ Reply ReplyRepository 에서 삭제 Method가 필요함
public interface BoardService{
  void removeWithReplies(Long bno);
}

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  //Board Repository 주입
  private final BoardRepository repository;
  //Reply Repository 주입
  private final ReplyRepository replyRepository;

  /***
   * 한번에 실행되어야 하는 트랜잭션이기에
   * @Transactional 어노테이션은 필수이다
   */
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

\- 게시글 수정🔽

```java
//java - Board Entity Class
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
    private Member writer;              //연관관계 지정 -- > 자동으로 커럼명은 writer_email 로 됨 변수명_PK 로 된다

    //////////////////////////////////////
    // Board 수정을 위한 Method
    //////////////////////////////////////
    // 하위 2개의 메서드는 게시글 수정에 사용된
    // Entity 자체 수정은 좋지 못하기에
    // Setter를 사용하지 않고 필요한 Method만을 사용함
    public void changeTitle(String title){
        this.title = title;
    }
    public void changeContent(String content){
        this.content = content;
    }

}


///////////////////////////////////////////////////////////////////////////


//java - BoardService, Impl

//Board Service
public interface BoardService{
  //게시글 수정
  void modify(BoardDTO boardDTO);
}


///////////////////////////////////////////////////////////////////////////


//Board ServiceImpl
@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
  private final BoardRepository repository;
  @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> board = repository.findById(boardDTO.getBno());

        // 해당 Board의 존재 유무를 확인
        // save()의 경우 Insert, Update 기능을 한번에 하기 떄문이다.
        if(board.isPresent()){
            Board result = board.get();
            // Board Entity Class에서 만들어준 값 변경
            // Method 사용
            result.changeTitle(boardDTO.getTitle());
            result.changeContent(boardDTO.getContent());
            repository.save(result);
        }//if
    }//modify
}

```

<hr/>
<br/>

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


///////////////////////////////////////////////////////////

//java - Controller
//목록 조회
@GetMapping("/list")
public void list(PageRequestDTO pageRequestDTO, Model model){
    log.info("list ::::" + pageRequestDTO);
    model.addAttribute("result",boardService.getList(pageRequestDTO));
}


///////////////////////////////////////////////////////////


// View("list") - HTML
<html>
  ...code..
  <table class="table table-striped">
      <thead>
      <tr>
          <th scope="col">#</th>
          <th scope="col">Title</th>
          <th scope="col">Writer</th>
          <th scope="col">Regdate</th>
      </tr>
      </thead>
      <tbody>

      <tr th:each="dto : ${result.dtoList}" >
          <th scope="row" >
              <a th:href="@{/board/read(bno = ${dto.bno}, page = ${result.page}
                                          , type = ${pageRequestDTO.type}
                                          , keyword = ${pageRequestDTO.keyword} )}">
                  [[${dto.bno}]]
              </a>
          </th>
          <td>
              [[${dto.title}]] ------- [<b th:text="${dto.replyCount}"></b>]
          </td>
          <td>[[${dto.writerName}]]<small>[[${dto.writerEmail}]]</small></td>
          <td>[[${#temporals.format(dto.regDate, 'yyyy/MM/dd')}]]</td>
      </tr>
      </tbody>
  </table>
  ..code...
</html>
```
