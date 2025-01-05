# M : N(다 대 다), @EntityGraph

## 1 ) M : N(다 대 다)

### 대표적인 예시
- 대표적인 예시
  - 1 ) 학생과 수업의 관계 
    - 한명의 학생은 여러 수업에 참여하고, 하나의 수업은 여러 학생이 수강 함
      - 학생 : 수업
  - 2 ) 상품과 상품 카테고리 
    - 하나의 상품은 여러 카테고리에 속하고, 하나의 카테고리는 여러상품을 가짐
      - 상품 : 카테고리
  - 3 ) 상품과 회원의 관계 
    - 하나의 상품은 여러 회원이 구매할수 있고, 한명의 회원은 여러 상품을 구매 함
      - 상품 : 회원
  - 4 ) 회원과 게시물 즐겨 찾기
    - 한명의 회원은 여러개의 게시물을 즐겨 찾기가 가능하가. 하나의 게시물은 여러 회원의 즐겨찾기 대상이 됨
      - 회원 : 즐겨찾기 게시물

## 2 ) M : N 문제점
-  M:N의 관계는 실제로 테이블로 설계할수가 없다는 문제가 있다. 
  - 테이블은 고정된 개수의 컬럼을 가지고 있기 때문에 수평적 확장이 불가능 하기 때문이다.
  - 한 컬럼에 다수의 값을 ","로 넣을 수 있지만 해당 방업은 **다대다 관계는 정규화에 어긋난다**
    - 데이터 무결성 관리가 어려워짐
    - 효율적인 쿼리 작성이 어렵다

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

 
> 위와 같이 여러개의 상품과 여러개의 카테고리가 존재하는 테이블이 있을 경우 특정한 상품에 대해서 카테고리 정보를 추가해야한다  
> 상품 하나가 '가전' + '주방', 또는 '가전' + '신혼' 으로 연관지어야 할경우 고정된 수의 컬럼으로는 처리가 불가능하다.
 
수평적 확장
------------------------------------->
  
| col1 | col2 |  col3 | col4 | col5 |
|-----:|:----:|------:|:----:|-----:|  
|  row | row  |   row | row  |  row | 
|  row | row  |   row | row  |  row |
|  row | row  |   row | row  |  row |
|  row | row  |   row | row  |  row |

## 3 ) M : N 문제 해결 방법
- 🤩 Mapping Table을 중간에 두어 해결이 가능하다
  - 연결 테이블이라고도 부른다
- 💬 쉽게 설명
  - (N>----<M)로 연결하기 어려우니 중간에 테이블을 두는것이다 **(N>---연결테이블---<M)**


## 4 ) 매핑 테이블 특징 ? 

- 매핑 테이블은 연결하려는 테이블들의 **중간**에서 **양쪽의 PK를 참조하는 형태**로 사용
  

## 5 ) JPA에서 N:M(다대다) 처리 방법 

```properties
# ℹ️ JPA에서 N:M을 처리하는 방법은 2가지가 있다
#    - 1 ) 각각의 Entitu 내에 @ManyToMany를 두어 연결 하는 방법
#    - 2 ) 중간에 연결 Entity를 두어서 각각의 PK를 FK로 @ManyToOne로 연결 하는 방법 
```

- ### 첫번째 방법 👎
  - 매핑 테이블을 **자동으로 생성**되는 방식이기에  주의가 필요함
    - JPA의 실행헤서 가장 중요한 것은 현재 컨텍스트의 엔티티 객체들의 상태와 데이터베이스의 상태를 **일치 시키는 것이 중요함**
      - 자동 생성된 매핑 테이블의 경우 일치 시키기에도 굉장히 어렵고 **컬럼 추가 및 관리가 굉장히 어렵다**
- ### 두번째 방법 👍
  -별도의 Entity Class를 설계하고, @ManyToOne을 사용하여 처리하는 방식  
    - 안정적으로 Entity 객체와 DB를 일치 시킬 수 있다. 

### Entity Class
```properties
# ℹ️ Mapping Table은 식별 관계, 비식별 관계 두가지 방법을 정해서 사용 가능하다.
#   > 비즈니스 모델이 간단하고 관계가 명확하게 정의된 경우에는 복합 키 방식(@EmbeddedId)을 사용하는 것이 좋습니다.
#       ㄴ 비즈니스 로직에 맞는 직관적인 데이터 모델을 제공하기 때문입니다.
#   > 성능이나 유지보수가 중요한 경우, 중간 테이블에 시퀀스를 사용하는 방식(@GeneratedValue)을 사용하는 것이 효율적입니다.
#       ㄴ 성능을 최적화하고, 코드가 더 간결하고 관리가 쉬워지기 때문입니다.
```
#### 식벽관계 방법
```java
@Entity
public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id; // 복합 키
  
    /***
     * 하위 2개의 Table을 연결 하는 필드
     * */
    @ManyToOne
    @MapsId("studentId") // 복합 키에서 studentId 필드를 사용
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @MapsId("courseId") // 복합 키에서 courseId 필드를 사용
    @JoinColumn(name = "course_id")
    private Course course;
}

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class StudentCourseId implements Serializable {
  private Long studentId;
  private Long courseId;
}
```
#### 비식벽관계 방법
```java
@Entity
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    /***
     * 하위 2개의 Table을 연결 하는 필드
     * */
    @ManyToOne
    @MapsId("studentId") // 복합 키에서 studentId 필드를 사용
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @MapsId("courseId") // 복합 키에서 courseId 필드를 사용
    @JoinColumn(name = "course_id")
    private Course course;
}
```

## 6 ) N + 1 문제

### N + 1 문제란?
- 특정 엔티티를 조회할 때, 1번의 쿼리로 메인 엔티티를 가져오지만, 해당 엔티티와 **연관된 다른 엔티티를 로딩**하기 위해 **추가적으로 N번의 쿼리가 발생**하는 문제
  - 부모 엔티티 1개를 조회한 뒤 자식 엔티티를 N번 각각 조회하는 상황을 의미

### N + 1 문제가 발생하는 과정
- 1 . 메인 엔티티 조회
  -  JPA는 먼저 **메인 엔티티를 조회**합니다. 예를 들어, 부모 엔티티(Parent)를 조회 시 **10개의 데이터가 조회** 된다 가정
    - `SELECT * FROM parent;`
- 2 . 연관된 엔티티를 N + 1 조회 
  - `@OneToMany` or `@ManyToOne` 관계로 설정되어 있는 경우 자식 엔티티를 조회하기 위해 별도의 쿼리를 실행
    - **즉시 로딩 fetch = FetchType.EAGER**일 경우에는 즉시, **지연 로딩 fetch = FetchType.LAZY**일 경우에는 해당 데이터를 부를 경우 발생
    - 결과적으로, 총 1번의 부모 쿼리와 **N(10)번의 자식 쿼리가 발생**
  ```text
  SELECT * FROM child WHERE parent_id = 1;
  SELECT * FROM child WHERE parent_id = 2;
  ...
  SELECT * FROM child WHERE parent_id = 10;
  ```
### 발생 이유

#### 지연 로딩(Lazy Loading)
- 초기에는 필요한 데이터를 최소한으로 가져오지만, 이후 연관된 엔티티를 참조할 때마다 추가 쿼리가 발생 하기 때문
#### 즉시 로딩(EAGER Loading)
- 조회 즉시 필요한 데이터를 바로 가져옴
#### JPA의 기본 동작
- JPA는 연관된 엔티티를 자동으로 로딩하지 않습니다. @OneToMany 또는 @ManyToOne 관계의 데이터를 명시적으로 페치(fetch)하지 않으면, 각 엔티티마다 추가 쿼리를 실행


### N + 1 문제 해결 방법

```properties
# ℹ️ 상황 및 조회 해야하는 상황에 맞게 적용해서 처리하는것이 좋다.
#   ㄴ 다만 규모가 클 수록 Querydsl(fetch Join)을 적용하자
```

- Fetch Join
- Entity Graph
- Sub Query 
- Querydsl  👍 
  - fetch Join
  - Sub Query 사용

### 비교
| **특징**              | **Entity Graph**                                           | **Fetch Join**                                      | **Sub Query**                                           |
|-----------------------|----------------------------------------------------------|---------------------------------------------------|--------------------------------------------------------|
| **사용 방식**          | JPA 표준 스펙으로 선언적 방식                               | JPQL로 작성, 코드에 의존적                         | JPQL로 작성, 서브 쿼리를 사용하여 특정 데이터를 로드    |
| **쿼리 작성 필요 여부** | 필요 없음 (`@EntityGraph`로 설정)                          | 직접 쿼리를 작성                                   | 직접 쿼리를 작성해야 함                                 |
| **유연성**             | 선언적이고 유지보수 쉬움                                    | 복잡한 조건의 조인에 적합                          | 특정 조건으로 필터링된 데이터를 가져오는 데 적합        |
| **페이징**             | 페이징 처리에 적합                                         | 페이징 처리 시 문제 발생 가능                      | 페이징 처리와 무관하게 사용할 수 있음                  |
| **다중 Fetch 제한**     | 여러 관계를 Fetch 가능, 하지만 무분별한 사용 시 성능 문제 발생| 여러 Fetch Join 사용 시 Cartesian Product 주의     | 관계 없이 서브 쿼리 내에서 필요한 데이터만 가져옴       |
| **SQL 최적화**         | ORM이 생성한 쿼리를 그대로 사용                             | 직접 Join 쿼리를 작성해 최적화 가능                | 서브 쿼리를 통해 필요한 데이터만 조회 가능              |
| **성능**              | 단순한 관계에서는 효율적                                    | 복잡한 관계의 데이터를 한 번에 로드할 때 효율적     | 적은 데이터에 대해 조건 기반 조회 시 효율적            |


#### 에제 Entity Class
```java
@Entity
public class Movie extends BaseEntity{
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mno;
  private String title;
}

@Entity
public class MovieImage {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long inum;
  private String uuid;
  private String imgName;
  private String path;
  
  @ManyToOne(fetch = FetchType.LAZY) 
  @ToString.Exclude
  private Movie movie;
}

@Entity
public class Member extends  BaseEntity{
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mid;
  private String email;
  private String pw;
  private String nickname;
}

// Movie -< Review >- m_member 구조
//  ㄴ>   FK 기준은 항상 외래키를 가지고 있는 테이블을 기준으로 작성하자!!
public class Review extends BaseEntity{
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewnum;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private Movie movie;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Setter
  private int grade;
  @Setter
  private String text;
}
```

### N + 1이 발생하는 JQPL
```java
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m" +                    //Movie 목록
            ", MAX(mi)" +                  //MovieImage
            ", AVG(coalesce(r.grade,0))" + // Review r 의 grade 값의 평균을 구함 coalesce -> Nvl 의 좀더 확작된 Oracle 함수
            ", COUNT(DISTINCT r) " +       // Review r 의 중복 제거 개수
            "FROM Movie m" +
            " LEFT OUTER JOIN MovieImage mi ON mi.movie = m" +
            " LEFT OUTER JOIN Review r ON r.movie = m group by m")
    Page<Object[]> getListPage(Pageable pageable);
}
```

### Sub Query를 사용한 처리
- 서브 쿼리를 사용하기에 성능상에는 조금 문제가 있음
```java
public interface MovieRepository extends JpaRepository<Movie, Long> {
  @Query("SELECT m , mi , COUNT(r) FROM Movie m " +
          "LEFT JOIN MovieImage mi ON mi.movie = m " +
          // 👍 아래와 같이 LEFT JOIN에 추가적으로 inum에 MAX값을 구하는 서브쿼리를 구한 후
          //    적용하는 방법으로 처리가 가능하다
          "AND mi.inum = (SELECT MAX(mi2.inum) FROM MovieImage mi2 WHERE mi2.movie = m) " +
          "LEFT OUTER JOIN Review r ON r.movie = m GROUP BY m")
  Page<Object[]> getListPageOrdeyByInum(Pageable pageable);
}
```

### EntityGraph를 사용한 처리
- 💬 @EntityGrpah란 ?
  - Entity의 틍정한 속성으 같이 로딩하도록 지정하는 어노테이션이다.
- 💬 @EntityGrpah 옵션
  - attributePath : 로딩 설정을 변경하고 싶은 속성의 이름을 **{"entity","entity"}**로 명시
  - type : 어떠한 방식으로 적용할 것인지 설정
- ✅ 간단하게 설명
  - @EntityGraph는 Repository에 적용하는 어노테이션
  - 데이터틑 불러올 때 로딩방식(FetchType)을 변경해 주는 것
- 단순한 관계에서는 효율적이나 복잡한 연관관계에서는 효율적
  - 무분별한 attributePaths 추가는 성눙 이슈가 생 가능함
```java
public interface ReviewRepository extends JpaRepository<Review, Long> {
  // 👍  findByMovie를 사용할 때 member 속성을 Eager로 로딩하게 끔 설정
  @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
  List<Review> findByMovie(Movie movie);
}  
```
### Fetch Join를 사용한 처리
- 💬 Fetch Join이란 ?
  - 연관된 Entity의 데이터를 한번에 불러 오는것이다.
- Cartesian Product 주의
  - DISTINCT 키워드 사용하여 조절 할 수 있음
- 페이징에서 사용 불가능
```java
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r " +
           "JOIN FETCH r.movie " +
           "JOIN FETCH r.member " +
           "WHERE r.movie.mno = :mno")
    List<Review> findReviewsWithFetchJoin(@Param("mno") Long movieId);
}
```

### QueryDsl - Sub Query를 사용한 처리
- MovieImage의 inum을 가져올경우 서브쿼리를 사용할때 :: JPAExpressions를 사용
  - tuple.groupBy(movie); 를 해주지 않으면 에러 발생 -> 단건이 아니기 때뮨
```java
@Log4j2
public class MovieSupportRepositoryImpl extends QuerydslRepositorySupport  implements MovieSupportRepository{

  public MovieSupportRepositoryImpl() { super(Movie.class); }

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
```

### QueryDsl를 사용한 처리
- `.fetchJoin()`을 사용하지 않아도 되는 이유은 `leftJoin()`를 사용하기에 이미 모든 데이터를 가져 오기 때문이다.
- `fetchJoin()`의 역할
  - fetchJoin()은 즉시 로딩을 강제하는 메서드입니다. 
  - fetchJoin()을 사용하면 연관된 엔티티를 한 번의 쿼리로 로드하므로, Lazy Loading으로 인한 N+1 문제를 방지할 수 있습니다.
```java
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

```

## 7 ) 썸네일 생성 [ Thumbnailator ]

### Dependencies 
```groovy
dependencies {
  // https://mvnrepository.com/artifact/net.coobird/thumbnailator
  implementation group: 'net.coobird', name: 'thumbnailator', version: '0.4.18'
}
```

### Thumbnailator 저장 

```java
@Controller
@Log4j2
public class UploadController {

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
    
    Path savePath = Paths.get(saveName);
    try {
      
      uploadFile.transferTo(savePath);

      // ✅ 썸네일 생성 Thumbnailator 사용
      //1) 파일명 생성 -- ✔ s_ 를 사용하여 구분함
      String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
              + "s_" + uuid + "_" + fileName;
      //2) File 객체 생성
      File thumbnailFile = new File(thumbnailSaveName);
      // Thumbnailator 를 사용하여 썸네일 생성 (File inFile[ Full Path + File 정보 ], File outFile, width, height)
      Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100,100);

      // 2 - 6 - 2 . 파일 저장 결과를 DTO에 저장 후 List에 Add해줌
      resultDTOList.add(UploadResultDTO.builder().fileName(fileName).folderPath(folderPath).uuid(uuid).build());
    }catch (Exception e){
      e.printStackTrace();
    }//try -catch
    
  }
    
}

```
