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

<h3>5 ) JPA에서 M:N(다대다) 처리 방법 </h3>
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
//TODO Review Table