<h1>M : N(다 대 다) [@EntityGraph], 파일업로드</h1>

<h3>1 ) M : N(다 대 다) 관계의 특징 </h3>

- 다 대 다 의 대표적인 예시
  - 1 ) 학생과 수업의 관계 :한명의 학생은 여러 수업에 창여하고, 하나의 수업은 여러 학생이 수강한다.
  - 2 ) 상품과 상품 카테고리 : 하나의 상품은 여러 카테고리에 속하고, 하나의 카테고리는 여러상품을 가지고 있다.
  - 3 ) 상품과 회원의 관계 : 하나의 상품은 여러 회원이 구매할수 있고, 한명의 회원은 여러 상품을 구매할 수 있다.

<br/>
<hr/>

<h3>2 ) M : N(다 대 다) 연관관계를 만들시  문제점 </h3>
-  M:N의 관계는 실제로 테이블로 설계할수가 없다는 문제가 있다. ☠️
  - 그 이유는 테이블은 고정된 개수의 컬럼을 가지고 있기 떄문이다.  

 💬 상품 Table

|  상품번호 |                 상품명                 |
|------:|:-----------------------------------:|
|     1 |                 냉장고                 |
|     2 |                 세탁기                 |
|     3 |                 TV                  |

💬 카테고리 Table

| 카테고리 |                 상품명                  |
|-----:|:------------------------------------:|
|   C1 |                  가전                  |
|   C2 |                  신혼                  |
|   C3 |                  주방                  |
|   C4 |                  영상                  |

- 위와 같이 테이블이 존재한다면 다대다 설계가 불간으하다. 이유는 위에서 말한것과 같은 고정된 개수의 컬럼을 가지고 있기 때문이다.  
 
> 위와 같이 여러개의 상품과 여러개의 카테고리가 존재하는 테이블이 있을 경우 특정한 상품에 대해서 카테고리 정보를 추가해야한다  
> 상품 하나가 '가전' + '주방', 또는 '가전' + '신혼' 으로 연관지어야 할경우 고정된 수의 컬럼으로는 처리가 불가능하다..
 
- 대부분의 관계형 데이터베이스는 테이블이라는 정형화된 구조를 가지는 방식으로 만들어져   
컬럼을 지정하면 컬럼의 개수 및 컬럼 내용의 최대 크기를 지정하기 때문에 수평적인 확장은 사실상 불가능하다. 

수평적 확장
------------------------------------->
  
| col1 | col2 |  col3 | col4 | col5 |
|-----:|:----:|------:|:----:|-----:|  
|  row | row  |   row | row  |  row | 
|  row | row  |   row | row  |  row |
|  row | row  |   row | row  |  row |
|  row | row  |   row | row  |  row |

<br/>
<hr/>

<h3>3 ) M : N(다 대 다) 연관관계 해결방안 [ Row를 사용 수직으로 확장하면 된다! ] </h3>

- Table은 Row라는 개념을 이용하여서 수직으로 확장이 가능하다.
- 실제 설계에서는 M:N(다대다)를 해결하기 위해 매핑(Mapping) 테이블을 이용하여 해결하고  
흔히 이것을 연결 테이블이라고도 부른다
- 💬 쉽게 설명해서 (다>----<다)로 연결하기 어려우니 중간에 테이블을 두는것이다 **(다>---연결테이블---<다)**

<br/>
<hr/>

<h3>4 ) 매핑 테이블의 특징 ? </h3>

- 매핑 테이블은 연결하려는 테이블 보다 먼저 존재햐아한다. 
- 매핑 테이블은 주로 "명사가 아닌 "동사" 혹은 "히스토리"에 대한데이터를 보관하는 용도이다.
- 매핑 테이블은 연결하려는 테이블들의 중간에서 양쪽의 PK를 참조하는 형태로 사용한다.
  
<br/>
<hr/>

<h3>5 ) JPA에서 M:N(다대다) 처리 방법 - 사전 준비 </h3>
- 첫번째 방법 : @ManyToMany를 사용하는 방식 👎
  - 해당 방법은 Entity와 매핑 테이블을 자동으로 생성되는 방식으로 처리되기에 주의가 필요하다.
  - JPA의 실행헤서 가장 중요한 것은 현재 컨텍스트의 엔티티 객체들의 상태와 데이터베이스의 상태를 일치 시키는것이 중요한데  
  자동으로 생성되는 매핑테이블은 일치 시키기에도 굉장히 어렵고 컬럼 추가 및 관리가 굉장히 어렵다
  - 💬 그렇기에 실무에서도 가능하다면 단방향 참조를 위주로 진행하거나 매핑 테이블 자체를 아예 Entity Class로 작성하여  
  각각의 연결 부분을 @ManyToOne로 연결 하여 수면위로 올려서 관리하는 방법을 사용한ㄷ.
- 두번째 방법 : 별도의 엔티티를 설계하고, @ManyToOne을 사용하여 처리하는 방식 👍 

<br/>

\- 사용될 Entity Classes 🔽  
💬 class 상단 어노테이션은 생략함
```java
//java - Entity Class

// Movie Class
public class Movie extends BaseEntity{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;

  private String title;

}


////////////////////////////////////////////////////////////////////////////


//MovieImage Class
public class MovieImage {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long inum;

  private String uuid;

  private String imgName;

  private String path;

  @ManyToOne(fetch = FetchType.LAZY) //lazy Type 로변경
  @ToString.Exclude //toString()에서 제외
  private Movie movie;
}


////////////////////////////////////////////////////////////////////////////


//Member Class
@Table(name = "m_member") // 이전 예저 프로젝트의 member 와 테이블명 중복으로 TableName 지정
public class Member extends  BaseEntity{
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mid;

  private String email;

  private String pw;

  private String nickname;
}
```

<br/>

\- 매핑 테이블인 Review Class 🔽    
👉 Review Class는 Movie와 Member를 ***양쪽으로 참조하는 매핑테이블 구조이므로 @ManyToOne으로*** 설계된다.  
👉 @ManyToOne으로 연결된 객체는 모두 ***fetch = FetchType.LAZY*** 로 설정해 줘야햔다.  
👉 연결된 객체들은 toString()으로 호출되지않게 exclude 시켜줘야한다.  
```java
//java - EntityClass [ Mapping Table Entity ]

/**
 * @Description : 해당 클래스는 @ManyToMany 대신에 사용하는 방법으로
 *                해당 Class는 중간 다리 역할을 하며 동시에 정보 기록까지 같이 할수있다.
 *
 *                해당 테이블은 매핑 테이블이라 하며 주로 동사나 히스토리를 의미하는 테이블이다
 *                 - 해당 예제에서는 회원이 영화에 대한 평점을 준다를 구성할때 여기서서 <b>평점을 준다</b>
 *                   부분이 해당 Class의 역할이라 볼수있다.
 *
 *                 - 해당 Entity 구조를 보면  Movie -< Review >- m_member
 *                   로 Review 테이블을 중간에 두고 서로를 연결하고 있는 구조이다.
 *
 *                 ✔ 여기서 잊으면 안되는 Tip
 *                    - FK 기준은 항상 외래키를 가지고 있는 테이블을 기준으로 작성하자!!
 * */
public class Review extends BaseEntity{
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewnum;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private Movie movie;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  private int grade;

  private String text;

  public void changeGrade(int grade){
    this.grade = grade;
  }

  public void changeText(String text){
    this.text = text;
  }

}
```

<br/>

- 💬 다대다 테스트에 앞서 준비된 데이터
  - Movie : 100개의 영화 목록 ***[ PK : mno ]***
  - MovieImage : Movie의 목록에 맞는 랜덤하게 매칭한 100개의 이미지 목록 ***[ FK : movie_mno ]***
  - Member : 100명의 회원 ***[ PK : mid ]***
  - Review : 200개의 영화 리뷰 (매핑 테이블) ***[ FK : member_mid, movie_mno ]***
- 💬 테스트 목록 - <b>JQPL을 사용하며 @EntityGraph, 서브 쿼리를 활용</b>
  - 1. **[ 목록 ]** 영화의 제목 + 영화 이미지 한개 + 영화 리뷰 개수, 평점 
  - 2. **[ 상세 ]** 영화 이미지들 + 리뷰 평점, 리뷰 개수
  - 3. **[ 상세 ]** 해당 리뷰에 대한 회원의 정보

<br/>
<hr/>
 
<h3>6 ) 상단에 명시된 테스트 목록 테스트</h3>

- 테스트의 이유 ? :: N + 1 상황과 @EntityGraph 사용 예시를 보기위함
1. **[ 목록 ]** 영화의 제목 + 영화 이미지 한개 + 영화 리뷰 개수, 평점  🔽 
```java
//java - Repository

//💬 N + 1 의 문제가 발생함 !
public interface MovieRepository extends JpaRepository<Movie, Long> {
  /**
   * ☠️ 아래의 JPQL Query에는 N+1문제가 있다.
   * 문제의 원인은 MAX(mi)에 있다.
   * - 이유 : 목룩울 가져오는 쿼리는 문제가 없지만 max()를 이용하는 부분에 들어가면 해당 영화의
   *          모든 이미지를 가져오는 쿼리가 실행되기 떄문이다.
   *
   * ✔ N+1 문제란 ?
   *  - 한번의 쿼리로 N개의 데이터를 가져왔는데 N개의 데이터를 처리하기 위해서 필요한 추가적인 쿼리가
   *    각각 N개의 대하서 수행되는 것임
   *
   *    쉽게 말하면
   *    - 해당 예제에서는 1페이지에 해당되는 10개의 데이터를 가여오는 쿼리 1번 실행 후
   *      각 영화의 모든 이미지를 가져오기 위한<b>Max()</b> 10번의 추가적인 쿼리가 실행되는것임
   *
   * 👍 해결 방법은 간단하게 Max() 집계함수를 사용하지 않는 것이다.
   *
   * - Movie m
   * - MovieImage mi
   * - Review r
   * */
  @Query("SELECT m" +                    //Movie 목록
          ", MAX(mi)" +                  //MovieImage
          ", AVG(coalesce(r.grade,0))" + // Review r 의 grade 값의 평균을 구함 coalesce -> Nvl 의 좀더 확작된 Oracle 함수
          ", COUNT(DISTINCT r) " +       // Review r 의 중복 제거 개수
          "FROM Movie m" +
          " LEFT OUTER JOIN MovieImage mi ON mi.movie = m" +
          " LEFT OUTER JOIN Review r ON r.movie = m group by m")
  Page<Object[]> getListPage(Pageable pageable);

  /*********************************************************************************************/  
  
  //👍 N + 1 의 문제가 해결 - 단 가장 최근것이 아닌 문제가있다. 😅
  /**
   * 👍 getListPage(Pageable pageable)에서 N+1 문제를 해결
   *
   * 👉 해결방법 Max()를 사용하지 않음
   * 
   * 💬 다만 간단하게 해결되긴 했지만 선택된 MovieImage는 inum가 가장 높은것이 아닌
   *    가장 나중(처음)에 들어온 기준으로 나오는 문제가있다.
   * */
  @Query("SELECT m" +                    //Movie 목록
          ", mi" +                       //MovieImage
          ", AVG(coalesce(r.grade,0))" + // Review r 의 grade 값의 평균을 구함 coalesce -> Nvl 의 좀더 확작된 Oracle 함수
          ", COUNT(DISTINCT r) " +       // Review r 의 중복 제거 개수
          "FROM Movie m" +
          " LEFT OUTER JOIN MovieImage mi ON mi.movie = m" +
          " LEFT OUTER JOIN Review r ON r.movie = m group by m")
  Page<Object[]> getListPageFix(Pageable pageable);

  
  /*********************************************************************************************/

  
  //👍 N + 1 의 문제가 해결 - 최근 MoiveImage를 가져옴  
  //💬 서브 쿼리를 사용하하여 성능상에는 조금 문제가 있다.
  @Query("SELECT m , mi , COUNT(r) FROM Movie m " +
          "LEFT JOIN MovieImage mi ON mi.movie = m " +
          // 👍 아래와 같이 LEFT JOIN에 추가적으로 inum에 MAX값을 구하는 서브쿼리를 구한 후
          //    적용하는 방법으로 처리가 가능하다
          "AND mi.inum = (SELECT MAX(mi2.inum) FROM MovieImage mi2 WHERE mi2.movie = m) " +
          "LEFT OUTER JOIN Review r ON r.movie = m GROUP BY m")
  Page<Object[]> getListPageOrdeyByInum(Pageable pageable);
  
}


////////////////////////////////////////////////////////////////////////////////////

  
//java - Repository

@Test
public void testListPage() {

    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
  
    /** Result Query
      Hibernate: 
         select
         movie0_.mno as col_0_0_,
         max(movieimage1_.inum) as col_1_0_,
         avg(coalesce(review2_.grade,
         0)) as col_2_0_,
         count(distinct review2_.reviewnum) as col_3_0_,
         movie0_.mno as mno1_1_,
         movie0_.moddate as moddate2_1_,
         movie0_.regdate as regdate3_1_,
         movie0_.title as title4_1_ 
         from
         movie movie0_ 
         left outer join
         movie_image movieimage1_ 
         on (
         movieimage1_.movie_mno=movie0_.mno
         ) 
         left outer join
         review review2_ 
         on (
         review2_.movie_mno=movie0_.mno
         ) 
         group by
         movie0_.mno 
         order by
         movie0_.mno desc limit ?
     
     ----------------------------------
     ☠️ 아래의 쿼리가 N+1 문제 발생 :: 이유 ? Max() 집계함수가 문제다 
      Hibernate:   👉 [ x10 번 ]
         select
         movieimage0_.inum as inum1_2_0_,
         movieimage0_.img_name as img_name2_2_0_,
         movieimage0_.movie_mno as movie_mn5_2_0_,
         movieimage0_.path as path3_2_0_,
         movieimage0_.uuid as uuid4_2_0_ 
         from
         movie_image movieimage0_ 
         where
         movieimage0_.inum=?
     **/
    
    Page<Object[]> result = movieRepository.getListPage(pageRequest);
      
    for (Object[] obj : result.getContent()) {
      log.info(Arrays.toString(obj));
    }
    
  }

  /*********************************************************************************************/

  
  @Description("N+1 문제를 해결 - 하지만 가장 처음 Movie Image를 가져오는 문제가 있음")
  @Test
  public void fixTestListPage(){
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    /** Result - Query
      Hibernate:
        select
        movie0_.mno as col_0_0_,
                movieimage1_.inum as col_1_0_,
        avg(coalesce(review2_.grade,
                0)) as col_2_0_,
        count(distinct review2_.reviewnum) as col_3_0_,
        movie0_.mno as mno1_1_0_,
                movieimage1_.inum as inum1_2_1_,
        movie0_.moddate as moddate2_1_0_,
                movie0_.regdate as regdate3_1_0_,
        movie0_.title as title4_1_0_,
                movieimage1_.img_name as img_name2_2_1_,
        movieimage1_.movie_mno as movie_mn5_2_1_,
                movieimage1_.path as path3_2_1_,
        movieimage1_.uuid as uuid4_2_1_
                from
        movie movie0_
        left outer join
        movie_image movieimage1_
        on (
                movieimage1_.movie_mno=movie0_.mno
        )
        left outer join
        review review2_
        on (
                review2_.movie_mno=movie0_.mno
        )
        group by
        movie0_.mno
        order by
        movie0_.mno desc limit ?
    **/
    Page<Object[]> result = movieRepository.getListPageFix(pageRequest);
    for(Object[] obj : result.getContent()){
      log.info(Arrays.toString(obj));
    }
  }


  /*********************************************************************************************/

  
  @Description("N+1 문제를 해결과 최근 MovieImage를 가져옴")
  @Test
  public void testListPageInumDesc(){
    PageRequest pageRequest = PageRequest.of(0,10,Sort.by("mno").descending());
    /**
    Hibernate:
      select
      movie0_.mno as col_0_0_,
              movieimage1_.inum as col_1_0_,
      count(review3_.reviewnum) as col_2_0_,
      movie0_.mno as mno1_1_0_,
              movieimage1_.inum as inum1_2_1_,
      movie0_.moddate as moddate2_1_0_,
              movie0_.regdate as regdate3_1_0_,
      movie0_.title as title4_1_0_,
              movieimage1_.img_name as img_name2_2_1_,
      movieimage1_.movie_mno as movie_mn5_2_1_,
              movieimage1_.path as path3_2_1_,
      movieimage1_.uuid as uuid4_2_1_
              from
      movie movie0_
      left outer join
      movie_image movieimage1_
      on (
              movieimage1_.movie_mno=movie0_.mno
              and movieimage1_.inum=(
                      select
              max(movieimage2_.inum)
              from
              movie_image movieimage2_
              where
              movieimage2_.movie_mno=movie0_.mno
      )
          )
      left outer join
      review review3_
      on (
              review3_.movie_mno=movie0_.mno
      )
      group by
      movie0_.mno
      order by
      movie0_.mno desc limit ?
     **/
    Page<Object[]> result = movieRepository.getListPageOrdeyByInum(pageRequest);
    result.getContent().stream().map(Arrays::toString).forEach(log::info);
  }

```

👉 번외 : 1번 테스트 Querydls 사용 🔽  
- 💬  아래  Querydls에서 유의깊게 보아야 하는 부분이 있다.
  - 1 . MovieImage의 inum을 가져올경우 서브쿼리를 사용할때 :: JPAExpressions를 사용하는것
  - 2 . tuple.groupBy(movie); 를 해주지 않아서 단건이 나왔던 문제 ,, << -- 해당 문제 때문에 삽질함..☠ ️
    - 해당 문제는 에러가 나지 않고 단건만 나올것을 예상하지 못했음 .. 
```java
// java - SupportImpl [Support Interface 및 Repository 상속 스킵 ]

// SupportImpl
@Log4j2
public class MovieSupportRepositoryImpl extends QuerydslRepositorySupport  implements MovieSupportRepository{

  public MovieSupportRepositoryImpl() {
    super(Movie.class);
  }

  @Override
  public Page<Object[]> getListWithQuerydsl(Pageable pageable) {

    // 1. Create QObject
    QMovie movie = QMovie.movie;
    QReview review = QReview.review;
    QMovieImage movieImage = QMovieImage.movieImage;

    // 2 . Create JPQLQuery
    JPQLQuery<Movie> jpqlQUery = from(movie);

    // 3 . Add Left Join
    jpqlQUery.leftJoin(movieImage)
            .on(movieImage.movie.eq(movie)
                    // 👍 SubQuery를 사용하여 inum의 Max()값을 가져옴
                    .and(movieImage.inum.eq(
                                    // 💬 SubQuery는 JPAExpressions을 사용해서 가져와야한다!!
                                    // 기존에처럼 from(movieImage)를 사용하여 사용할 경우 Error는 없지만
                                    // 첫 데이터 한줄의 MoiveImage값은 나오고
                                    // 그 이후 데이터는 null로 박혀서 나옴!!
                                    JPAExpressions
                                            .select(movieImage.inum.max())
                                            .from(movieImage)
                                            .where(movieImage.movie.eq(movie))
                            )
                    )
            );
    jpqlQUery.leftJoin(review).on(review.movie.eq(movie));

    // 4 . Create JPQLQuery<Tuple>
    JPQLQuery<Tuple> tuple = jpqlQUery.select(movie, movieImage, review.grade.avg(), review.countDistinct());




    // 5 . Sorting
    Sort sort = pageable.getSort();
    sort.stream().forEach(order ->{
      // get Sort Key
      String prop = order.getProperty();
      // get Sort Type
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      // crate PathBuilder
      PathBuilder<Movie> orderByExpression = new PathBuilder<Movie>(Movie.class,"movie");
      // apply Sort for Tuple
      tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
    });


    // ☠️ group By를 해주지 않을 경우 단건이 나옴!!! [ 삽질함!! ]
    tuple.groupBy(movie);

    // 6 . Apply Pageable
    tuple.offset(pageable.getOffset());
    tuple.limit(pageable.getPageSize());

    // 7 . Parse List
    List<Tuple> result = tuple.fetch();

    // 8 . get Count - 💬 Reason : Use PageImpl Parameter
    long count = tuple.fetchCount();

    //9 . Return PageImpl : needs Three Parameter ( List , Pageable , Long )
    return new PageImpl( result.stream().map(Tuple::toArray).collect(Collectors.toList())
            , pageable
            , count);
  }
}

//////////////////////////////////////////////////////

// Result Query
/**
Hibernate:
        select
          movie0_.mno as col_0_0_,
          movieimage1_.inum as col_1_0_,
          avg(review3_.grade) as col_2_0_,
          count(distinct review3_.reviewnum) as col_3_0_,
          movie0_.mno as mno1_1_0_,
          movieimage1_.inum as inum1_2_1_,
          movie0_.moddate as moddate2_1_0_,
          movie0_.regdate as regdate3_1_0_,
          movie0_.title as title4_1_0_,
          movieimage1_.img_name as img_name2_2_1_,
          movieimage1_.movie_mno as movie_mn5_2_1_,
          movieimage1_.path as path3_2_1_,
          movieimage1_.uuid as uuid4_2_1_
        from
          movie movie0_
        left outer join
          movie_image movieimage1_
          on (
            movieimage1_.movie_mno=movie0_.mno
            and movieimage1_.inum=(
              select
                max(movieimage2_.inum)
              from
                movie_image movieimage2_
              where
                movieimage2_.movie_mno=movie0_.mno
              )
            )
        left outer join review review3_
        on (
            review3_.movie_mno=movie0_.mno
        )
        group by
            movie0_.mno
            order by
        movie0_.mno desc limit ?
**/
```
<br/>

2. **[ 상세 ]** 영화 이미지들 + 리뷰 평점, 리뷰 개수 🔽

```java
//java - Repository

// 🔽  Movie + MovieImage + Review
@Query("Select m" +                     // Movie
        ", im" +                        // MovieImage
        ", avg(coalesce(r.grade,0))" +  // Review grade
        ", count(r)" +                  // Review Count
        "from Movie m" +
        " left outer join MovieImage im on im.movie = m" +
        " left outer join Review r on r.movie = m " +
        "where m.mno = :mno  group by im")
    List<Object[]> getMovieWithAll(Long mno);


///////////////////////////////////////////////////////////////////


// 👉 Querydsl Version
public class MovieSupportRepositoryImpl extends QuerydslRepositorySupport  implements MovieSupportRepository{
    
    public List<Object[]> testGetMovieWithAllQuerydls(Long mno) {
      QMovie movie = QMovie.movie;
      QReview review  = QReview.review;
      QMovieImage movieImage = QMovieImage.movieImage;
  
      JPQLQuery<Movie> jpqlQuery = from(movie);
  
      jpqlQuery.where(movie.mno.eq(mno));
  
      jpqlQuery.leftJoin(movieImage).on(movieImage.movie.eq(movie));
      jpqlQuery.leftJoin(review).on(review.movie.eq(movie));
  
      //💬 Movie가 아닌 MovieImage로 Group By 해줘야함
      jpqlQuery.groupBy(movieImage);
  
      List<Tuple> result = jpqlQuery.select(movie, movieImage, review.grade.avg(), review.count()).fetch();
  
      return result.stream().map(Tuple::toArray).collect(Collectors.toList());
    }
    
}


///////////////////////////////////////////////////////////////////


/** Result Query 
      Hibernate:
        select
          movie0_.mno as col_0_0_,
          movieimage1_.inum as col_1_0_,
          avg(coalesce(review2_.grade,
          0)) as col_2_0_,
          count(review2_.reviewnum) as col_3_0_,
          movie0_.mno as mno1_1_0_,
          movieimage1_.inum as inum1_2_1_,
          movie0_.moddate as moddate2_1_0_,
          movie0_.regdate as regdate3_1_0_,
          movie0_.title as title4_1_0_,
          movieimage1_.img_name as img_name2_2_1_,
          movieimage1_.movie_mno as movie_mn5_2_1_,
          movieimage1_.path as path3_2_1_,
          movieimage1_.uuid as uuid4_2_1_
        from
            movie movie0_
        left outer join
            movie_image movieimage1_
        on (
            movieimage1_.movie_mno=movie0_.mno
            )
        left outer join
            review review2_
        on (
            review2_.movie_mno=movie0_.mno
        )
        where
            movie0_.mno=?
        group by
            movieimage1_.inum
**/
```
<br/>

3. **[ 상세 ]** 해당 리뷰에 대한 회원의 정보  ***[ 💬 @EntityGraph 사용 예제임  ]***

\- ☠️ Review Entity의 변수 중 Member가 FetchType.LAZY 방식이기에 발생하는 **no Session** Error 🔽
```java
//java 

//Review Entity
@Entity
public class Review extends BaseEntity{
  
  //... code ..  
  
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  
  //... code ..
}


///////////////////////////////////////////////////////////////////


//ReviewRepository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  List<Review> findByMovie(Movie movie);
}


///////////////////////////////////////////////////////////////////


//java - JUnit Test
@Test
public void testGetMovieReviews(){
  Movie movie = Movie.builder().mno(90L).build();

  List<Review> result = reviewRepository.findByMovie(movie);

  result.forEach(data -> {
    // ↓ Review 자체의 Data에 접근시 에는 문제가 없음 
    log.info(data.getReviewnum());
    log.info("--------------------");
    log.info(data.getGrade());
    log.info("--------------------");
    log.info(data.getText());
    log.info("--------------------");
    
    // ↓ 아래의 getMember에 접근시 Session Error가 발생한다 . ☠️
    log.info(data.getMember().getEmail());
    log.info("--------------------");
  });
}


///////////////////////////////////////////////////////////////////


// 👍 그렇다면 해결방안 ? 
// @Transactional 어노테이션을 사용한다.
// 💬 다만 성능상 문제가 있을 수 있다. Lazy 방식이기에
//    하나의 실행 단위로 묶는 Transaction을 사용하지만
//    그렇기에 해당 데이터에 ☠접근할때마다 조회하는 문제가 있다. 👎  
  
@Test
@Transactional  // 💬 근본적인 no Session Error를 해결할수 있지만  
                //    해당 데이터를 찾을 때마다 조회한다는 문제가 있다.
public void testGetMovieReviews(){
  Movie movie = Movie.builder().mno(90L).build();
  List<Review> result = reviewRepository.findByMovie(movie);
  result.forEach(data -> {
    // no Session Error 해결
    log.info(data.getMember().getEmail());
  });
}

```

<br/>

\- 👍 @EntityGraph를 사용하여 처리하는 방식
- @EntityGraph를 사용하는 방식 말고도 JPA내장 NameMethod를 사용하지말고 @Query를 사용하여 처리하는 방법도있다.
- 하지만 @EntityGraph를 사용하는 것이 더 간편하고 직관적 이므로 해당 방법을 사용한다.
- 💬 @EntityGrpah란 ?
  - Entity의 틍정한 속성으 같이 로딩하도록 지정하는 어노테이션이다.
  - JPA에서 연관 관계를 지정한 속성을 FetchType.LAZY로 지정하는것이 일반적이나 @EntityGraph를 사용하면  
    특정 기느을 수행할 때만 EAGER Type으로 지정하여 실행을 가능하게 끔해주는 설정이다.
- 💬 @EntityGrpah 옵션 
  - attributePath : 로딩 설정을 변경하고 싶은 속성의 이름을 **배열로 명시**
  - type : 어떠한 방식으로 적용할 것인지 설정
- ✅ 간단하게 설명하면 @EntityGraph는 Repository에 적용하는 어노테이션이고  
     원하는 Repository에서 데이러틑 불러올 때 로딩방식(FetchType)을 변경해 주는 것이다.
  
```java
//java 

//Review Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  // 👍  findByMovie를 사용할 때 member 속성을 Eager로 로딩하게 끔 설정
  @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
  List<Review> findByMovie(Movie movie);
}  


//////////////////////////////////////////////////////

//JUnit Test Code
@Test

public void testGetMovieReviews(){
  Movie movie = Movie.builder().mno(90L).build();
  List<Review> result = reviewRepository.findByMovie(movie);
  result.forEach(data -> {
    // no Session Error 해결 및 성능상에도 문제 없음
    log.info(data.getMember().getEmail());
  });
}


//////////////////////////////////////////////////////

//Result Query
/**
Hibernate:
      select
        review0_.reviewnum as reviewnu1_3_0_,
        member1_.mid as mid1_0_1_,
        review0_.moddate as moddate2_3_0_,
        review0_.regdate as regdate3_3_0_,
        review0_.grade as grade4_3_0_,
        review0_.member_mid as member_m6_3_0_,
        review0_.movie_mno as movie_mn7_3_0_,
        review0_.text as text5_3_0_,
        member1_.moddate as moddate2_0_1_,
        member1_.regdate as regdate3_0_1_,
        member1_.email as email4_0_1_,
        member1_.nickname as nickname5_0_1_,
        member1_.pw as pw6_0_1_
      from
        review review0_
            left outer join
                m_member member1_
            on review0_.member_mid=member1_.mid
      where
        review0_.movie_mno=?
**/
```

<br/>
<hr/>

<h3>7 ) 파일 업로드 처리</h3>

- 파일 업로드를 위한 setting 🔽
```properties
## application.properties

#####################
#File upload Setting#
#####################

#upload Y/N Set
spring.servlet.multipart.enabled=true

#upload file tmp dir
spring.servlet.multipart.location=C:\\upload

# Max Request Size
spring.servlet.multipart.max-request-size=215MB

# Max file size.
spring.servlet.multipart.max-file-size=200MB

#Real Upload Dir Path # Used call Java Code  
org.zerock.upload.path = C:\\upload
```

<br/>

- Client Upload 🔽
```html
<!-- html -->
<body>
    <!-- multiple 설정 필수 -->
    <input name="uploadFiles" type="file" multiple>
    <button class="uploadBtn">upload</button>   
</body>

<!-- javascript -->
<script>
document.querySelector(".uploadBtn").addEventListener("click",()=>{

            // 1. form 객체 생성
             let formData = new FormData();
            // 2. input name="uploadFiles"에 업로드 된 File을 읽음
             let inputFile = document.querySelector("input[name='uploadFiles']").files;

             // 2 . File을 form에 append 시킴
             for(let i of inputFile){
                formData.append("uploadFiles",i);
             }//for

            //비동기 통신 -ajax
            $.ajax({
                url : '/uploadAjax',
                processData : false,
                contentType : false,
                data : formData,
                type : 'post',
                dataType : 'json',
                success : function(result){
                 console.log(result);                 
                },
                error : function(jqXHR, textStatus, errorThrown ){
                    console.log(textStatus);
                }
            })

        });// click    
</script>

```

<br/>

- Server Upload Logic 🔽
```java
//java

@Controller
@Log4j2
public class UploadController {
  /**
   * 💬 반환 값이 없을 경우
   * Error 발생 : Error resolving template [uploadAjax], template might not exist or might not be accessible by any of the configured Template Resolvers
   * */
  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

    // 1 .  결과 값을 반환할 List<> 생성
    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    log.info("-----------------------");
    log.info(uploadFiles);
    log.info("-----------------------");

    // 2 . File의 개수 만큼 Loop
    for(MultipartFile uploadFile : uploadFiles){

      // 2 - 1 . 👉 MultipartFile.getContentType()를 사용하여 확장자를 체크가 가능함
      if(!uploadFile.getContentType().startsWith("image")){
        log.warn("this file is not image type");
        //이미지가 아닐경우 403 Forbidden Error
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }//if

      // 2 - 2 . 👉 IE 나 Edge 에서는 전체 경로가 들어오므로
      //            MultipartFile.getOriginalFilename()를 사용하여
      //            파일명을 잘라서 사용
      String originalFile = uploadFile.getOriginalFilename();
      String fileName = originalFile.substring(originalFile.lastIndexOf("\\")+1);

      log.info("fileName ::: " + fileName);

      // 2 - 3 . 날짜 폴더 생성 :: 반환값 ? 오늘 날짜의 파일 경로
      String folderPath = this.makeFolder();

      // 2 - 4 . 파일명 중복방지를 위한 UUID 생성
      String uuid = UUID.randomUUID().toString();

      // 2 - 5 . 전체 파일 명 -> + UUID + 구분자 _ 사용 하여 👉 FullPath + FileName 생성
      // 💬 문자열 내용 :: RootPath + Dir 구분자 + 오늘 날짜 폴더 Dir + Dir 구분자
      String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

      // 2 - 6 . 위에서 만든 FullPath 정보로 Path 객체 생성
      Path savePath = Paths.get(saveName);
      try {
        // 2 - 6 - 1 . 파일정보를 토대로 ==> FullPath로 변환(저장)
        uploadFile.transferTo(savePath);
        
        // 2 - 6 - 2 . 파일 저장 결과를 DTO에 저장 후 List에 Add해줌
        resultDTOList.add(UploadResultDTO.builder().fileName(fileName).folderPath(folderPath).uuid(uuid).build());
      }catch (Exception e){
        e.printStackTrace();
      }//try -catch

    }//end loop

    return ResponseEntity.ok().body(resultDTOList);
  }

  
  /**
   * @Description : 오늘 날짜로 Directory를 만는 Method
   *
   * */
  private String makeFolder(){
    // 1 . "yyyy/MM/dd" 패턴으로 현재 날짜를 받아옴
    String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    // 2 . "/" 를 Replace하여 운영체제에 맞는 파일 경로로 변경함
    String folderPath = str.replace("/", File.separator);

    // 3 . File 객체 생성 ( RootDir , 오늘 날자경로 )
    File uploadPathFolder = new File(uploadPath, folderPath);

    // 4 . Server에 uploadPathFolder 객체의 정보에 맞는  Directory가 있는지 확인
    if(!uploadPathFolder.exists()){
      // 4 - 1 . 없을 경우 해당 경로에 맞는 Directory 생성
      boolean success = uploadPathFolder.mkdirs();
    }//if

    //파일 경로 반환
    return folderPath;
  }
  
}

```