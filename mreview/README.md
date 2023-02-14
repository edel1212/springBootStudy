<h1>M : N(다 대 다) , 파일업로드</h1>

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

<h3>6 ) 상단에 명시된 테스트 목록</h3>

- 테스트의 이유 ? :: N + 1 상황과 @EntityGraph 사용 예시를 보기위함
1. 영화의 제목 + 영화 이미지 한개 + 영화 리뷰 개수, 평점 🔽 
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
}


```