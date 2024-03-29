<h1>JPA, ORM , Paging  - 👉( Docker - MaraiDB 사용 추가 )</h1>

<h3>1 ) ORM [ 객체 관계 매핑 (Object–relational mapping) ]❔</h3>

- <b>Class</b>와 <b>RDB(Relational DataBase)</b>의 테이블을 <b>매핑(연결)</b>한다는 뜻이며, 기술적으로는 어플리케이션의 객체를
  <br/>RDB 테이블에 자동으로 영속화 해주는 것이라고 보면된다.
  <br/>간단하게 말하면 <b>"객체지향 패러다음을 관계형 데이터베이스에 보존하는 기술"</b>이다
  <br/>패러다임 입장에서 생각하자면 객체지향 패러다임을 관계형 패러다임으로 <b>매핑해주는 개념</b>으로 볼수 있다
  <br/> 쉽게 말하면 Class(객체)의 구조로 Table 만들어 사용한다는 것이다.

```java
//SQL
create table (
    id varchar
    , pw varchar
    , name varchar
)

/////////////////////////////////

//Java
public Class People{
    private String id;
    private String pw;
    private String name;
}
```

- Class , Row 차이❔
  <br/>상단의 코드와 같이 Class와 Table이 상당히 유사한 구조이며 인스턴스(Class)와 Row(튜플)도 상당히 유사하다.
  <br/> 객체지향에서는 인스턴스를 생성해서 인스턴스라는 공간에 데이터를 보관하며, 테이블에서는 하나의 Row에
  <br/> 데이터를 저장한다는 차이점이 있고 둘의 개념적 차이를 보자면
  <br/> - Class : <b>데이터 + 행위(메서드)</b>를 의미한다.
  <br/> - Row&nbsp; : <b>데이터만</b>을 의미한다.

- ORM의 장점
  <br/>- SQL 문이 아닌 Method를 통해 DB를 조작 함으로 개발자는 객체 모델을 이용해 비즈니스 로직 구성하는데만 집중이 가능함.
  <br/>- 객체 지향적인 코드 작성이 가능하다. 오직 객체 지향적 접근만을 고려하면 되기 때문에 생산성 증가.
  <br/>- 매핑하려는 정보가 Class(Entity)로 명시 되어있기에 ERD를 보는 의존도를 낮출수 있고 유지보수 및 리펠토링에 유리함.
  <br/>- DB의 종류에 의존적이지 않음 MySQL -> Oracle 변경 시 SQL문을 사용하지 않았기에 수정이 필요 없다.

- ORM의 단점
  <br/>- 테이블간 관계가 복잡하여 설계가 잘못된 경우, 속도 저하 및 일관성을 무너뜨리며 관계를 맺기가 어려워짐
  <br/>- 복잡하고 성능상 불필요한 Query가 생성되는경우 [ Ex) N + 1 문제 등..] 결국은 Query 문을 사용해야할 경우도 생길 수 가있음.

<br/>
<hr/>

<h3>2 ) JPA(Java Persistence API)❔</h3>

- ORM을 Java언어에 맞게 사용하는 인터페이스 모음이다.
  <br/> 따라서 ORM 이 좀 더 <b>상위 개념</b>이고 ORM을 기술 표준에 맞게 사용하는 인터페이스 개념으로 볼수 있다
  <br/>
- JPA는 인터페이스 이기 때문에 Hibernate, OpenJPA 등이 JPA를 구현하고있고
  <br/>Spring Framework에서는 <b>"Hibernate"</b>를 대표적으로 사용하고 있다.

<br/>
<hr/>

<h3>3 ) JPA, DB 추가 및 DB접속 정보 설정</h3>
- 👿 주의사항 : Database를 dependency에 추가 시 접속 정보 Setting을 하지 않으면 빌드에러가 발생한다.
<br/>
- DB 설정 추가
  <br/>
  1 . 원하는 Databease를 build.gradle에 Dependency 추가
  <br/> ✅추가 설정 : JPA Query Deltail을 보기 위한 <b>p6spy-spring-boot dependecy</b> 추가
  <br/> &nbsp;&nbsp;&nbsp;&nbsp;ㄴ> 개발 및 테스트 시에는 그냥 사용해도 좋지만 운영으로 넘어갈 시
  <br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>application.properties에서 p6spy.enable-logging를 false로</b> 해주자.

```gradle
//build.gradle

dependencies {
//code...

// JPA SQL Derail Show
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.8'

    /**
     * MaraiaDB setting
     *  */
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation group: 'org.mariadb.jdbc', name: 'mariadb-java-client'

}

```

2 .Database의 접속 정보 추가

```properties
#application.propterties

######################
# datasource setting #
######################
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3310/bootex
spring.datasource.username=root
spring.datasource.password=123

##########################################################
# Detail Show JPA Query                                  #
# have to setting - Dev [true] - Prod [false] setting    #
##########################################################
decorator.datasource.p6spy.enable-logging=true

```

<br/>
- Spring Data JPA 설정
<br/>
- 주의할 점👿 : 실제 배포시에는 꼭 ddl-auto를 none으로 해야 합니다.
<br/>&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ddl-auto의 경우 초기 DB 설정 및 간단한 테스트에서만 
쓰는게 좋습니다.
</br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - 간단하게 설명
<br/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 운영 장비에서는 절대 crate, create-drop, update 사용하면 안된다.
<br/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 개발 초기 단계는 create 또는 update
<br/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 테스트 서버는 update 또는 validate
<br/>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; * 스테이징과 운영 서버는 validate 또는 none

```properties
## 기본적으로 Database 설정이 되어 있어야 한다!

######################
#   JPA setting      #
######################

# 프로젝트 실행 시 에 자동으로 DDL을 생성할 것인지 결정하는 설정이다.
# 설정값 : create         - 프로젝트 실행 시 매번 테이블을 생성한다.[ create 옵션은 해당하는 테이블이 있으면 DROP하고 새로 만들어 버린다 ]
#          update         -  프로젝트 실행 시 테이블이 없으면 만들어주고 있는데 수정이 필요할 경우 alter 해준다. [ 운영DB에서는 사용하면 안됨 ]
#          create-drop    - create와 비슷하지만 서버 종료시점에 테이블 DROP 시켜버린다.
#          validate       - 엔티티와 테이블이 정상 매핑되었는지만 확인한다.
#          none           - 사용하지 않음.
spring.jpa.hibernate.ddl-auto = update

# 실제 JPA의 구현체인 Hibernate가 동작하면서 발생하는 SQL을 포맷팅 하여 콘솔에 출력해준다.
spring.jpa.properties.hibernate.format_sql = true

# JPA 처리 시에 발생하는 SQL을 보여줄지 설정
spring.jpa.show-sql = true

```

<br/>
<hr/>

<h3>4 ) Entity Class란❔</h3>

- JPA를 통해서 관리하게 되는 객체를 위한 Class 이다.
  <br/>
- 해당 Class를 기반으로 Database의 Table이 만들어진다.

```java
//java

/**
 * @Description : @Entity - ✔ Spring Data JPA의 interface를 사용하기 위해서는 해당 어노테이션이 필수이다.
 *                          ✔ 해당 클래스가 엔티티를 위한 클래스라는것을 표시함
 *                          ✔ 해당 클래스의 인스턴스들이 JPA로 관리되는 객체라는 것을 명시해줌
 *
 *              : @Table  - ✔ @Entity 어노테이션과 같이 사용할 수있는 어노테이션을 단어 그대로 테이블명 설정 뿐만아니라 인덱스 등을 생성하는 것도 가능하다.
 *
 *              : @Id 와 @GeneratedValue - ✔ @Id❔             - @Entity가 붙은 엔티티클래스는 PK에 해당하는 특정 필드를 지정하는것 [ 필수! ]
 *                                         ✔ @GeneratedValue❔ - @Id[PK]가 설정된 Key가 사용자가 입력하는 값이 아닌 자동으로
 *                                                               생성되는 번호를 사용하는 컬럼으로 설정함
 *
 *                                         ✔ @GeneratedValue(strategy = GenerationType.???) 종류 ❔
 *                                           - GenerationType.AUTO     : JPA 구현체가 생성 방식을 결정하게함 (Default)
 *                                           - GenerationType.IDENTITY : 사용하는 데이터베이스가 키 생성을 결정
 *                                           - GenerationType.SEQUENCE : 데이터베이스의 sequence 이용해서 결정 @SequenceGenerator 와 같이 사용
 *                                           - GenerationType.TABLE    : 키 생성 전용 테이블을 생성해서 키 생성. @TableGenerator 와 같이 사용
 *
 *              : @Column - ✔ 컬럼의 옵션이 필요한 경우 해당 어노테이션을 추가하여 설정이 가능하다.
 *                          ✔ 해당 어노테이션에는 다양한 속성이 추가 가능하다 [ 주로 사용되는것 ]
 *                            - nullable         : null 여부를 boolean으로 지정
 *                            - name             :객체 변수명과 Table의 컬럼명을 다르게 하고 싶은 경우 사용한다.
 *                            - length           : 컬럼의 길이
 *                            - columnDefinition : 기본값 지정
 *
 *              : Entity Class의 기본적 세팅 어노테이션
 *                  @Getter
 *                , @Builder
 *                , @AllArgsConstructor
 *                , @NoArgsConstructor - ✔ @Builder 를 이용해서  객체를 생성하기위해 AllArg, NoArg Constructor
 *                                         같이 사용해줘야 컴파일 시 에러가 없다. ---- 추가 사항 생성자를 따로 추가해서 PK값을
 *                                         않받고 builder 패턴 적용이 가능함!
 *
 *             : 주의사항 - @Data , @Setter 를 사용하지 않는 이유는 Entity Class의 값은 직접적인 Class의
 *                         수정이 적을 수록 추후 개발에 용의 하기 때문이다
 *
 * */
@Entity
@Table(name = "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;


     /**
     * 아래와 같이 사용하면 mno의 경우 PK임! 따라서 속성 설정이
     * GenerationType.IDENTITY인 값을 Builder pattern 에서 제외가 가능함
     *
     * 또한 생성자를 추가 줬기 때문에 @AllArgsConstructor 를 사용하지 않아도 된다!
     *
     * 단 현제 프로젝트에서는 DTO를 사용하지 않기 때문에 Error 가 발생함으로 주석처리함.
     * */
    // @Builder
    // public Memo(String memoText){
    //     this.memoText = memoText;
    // }

}
```

<br/>
<hr/>

<h3>5 ) JpaRepository란❔</h3>

- Spring Data JPA 는 구현 체인 Hibernate를 이용하기 위한 여러가지 API를 제공하는데,
  <br/> 그 중에서 가장 대표적으로 사용되는것이 JpaRepository Interface 이다.
- Spring Data JPA에는 여러 종류의 인터페이스 기능을 통해 JPA 관련 작업을 별도의 코드 없이도 진행이
  <br/> 가능하도록 지원합니다.
- 간단한 CRUD 작업 혹은 페이징, 정렬 드으이 처리도 인터페이스의 메서드를 호출하여 처리가 가능하고
  <br/> 필요에 따라서 상속 구조로 추가적인 기능도 제공합니다.
- ✅중요
  <br/> 1 . Repository는 <b>Interface</b>이다!
  <br/> 2 . JpaRepository를 <b>JpaRepository</b>를 상속 받아야 기능이 사용 가능하다
  <br/> 3 . <b>extends JpaRepository</b> 상속을 받는 것 만으로도 Bean Conatainer에 등록 대상이된다.

```java
//java - interface

/**
*  @Description : 1) JpaRepository는 인테페이스이며 Spring Data
 *                   JPA는 이를 상속하는 인터페이스를 상속하는 인터페이스를 선언하는 것만으로
 *                   모든 처리가 끝난다.
 *
 *                2) JpaRepository를 사용할 때는 엔티티의 타입정보(Meno) 와 @Id의 타입을 제네릭에
 *                   지정해 줘야한다 < Memo , Long >  :: < 엔티티Class , PK Type >
 *
 *                3) Spring Data JPA는 인터페이스 선언 만으로도 자동으로 빈에 등록된다.
 *
* */
public interface MemoRepository extends JpaRepository<Memo, Long> {
}

```
<br/>
<hr/>

<h3>6 ) JpaRepository를 사용한 CRUD</h3>

- 1 . Create : save()를 사용하여 Create 한다.

```java
//java JUnit Test Code - Create Test

@Test
public void testInsertDummies(){
    IntStream.range(1,100).forEach(i->{
        // Builder Pattern을  통해 Entity 객체 생성 후
        Memo momo = Memo.builder().memoText("Sample..." + i).build();
        memoRepository.save(momo); //save()에 Entity Class를 주입해  Insert 함!
    });
}
```

- 2 . Read : findById()를 사용하여 Read 한다.
  <br/> - ❌또 다른 조회 방법 중 <b>getOne()의 경우 Deprecated 되어 더는 사용하지 않음</b>을 권장한다!
  <br/> - findById()의 경우 반환 값이 Optional\<T>이다

```java
//java JUnit Test Code - Read Test

@Test
public void testSelect(){
    Optional<Memo> result =  memoRepository.findById(50L); // 찾을 PK 값
    System.out.println("===========================================");
    if(result.isPresent()){      //유무 체크
        Memo memo = result.get();
        System.out.println(memo);
    }
}
```

- 3 . Update : save()를 사용하여 Update 한다.
  <br/> - Insert 와 Update 같은 save()를 사용한다. PK 값 체크 후 없으면 Inert 있으면 Update 방식이다.

```java
//java JUnit Test Code - Update Test

/**
     * @Description : update 도 insert 와 마찬가지로 save()를 사용한다
     *                해당 테스트 실행 log를 보면
     *
     *                먼저 Select가 된 후
     *                Hibernate:
     *                  select
     *                      memo0_.memo_text as memo_tex2_0_0_
     *                      memo0_.mno as mno1_0_0_,
     *                  from
     *                      tbl_memo memo0_
     *                  where
     *                      memo0_.mno=?
     *
     *                해당 데이터가 있으면 update 없으면 Insert 한다!
     *                Hibernate:
     *                  insert
     *                      into
     *                  tbl_memo
     *                      (memo_text)
     *                  values
     *                      (?)
     *
     *              ✔ 이러한 구동의 원리는 JPA는 엔티티의 객체들을 메모리상에 보관하려하기 때문에
     *                특정한 엔티티 객체가 존재하는지 확인하는 Select 가 먼저 실행 되고 해당
     *                @Id를 가지는 엔티티 객체가 있다면 update 혹은 Insert가 실행되는 것이다.
     *
     *
     * **/
    @Test
    public void testUpdate(){
        Memo memo = Memo.builder()
                        .mno(100L)
                        .memoText("update Text ! Test !!!!")
                        .build();
        System.out.println(memoRepository.save(memo));
    }
```

- 4 . Delete : deleteById()를 사용하여 Delete 한다.
  <br/> - 해당 메서드의 return 타입은 void 이며 삭제하려는 PK가 없을 시 EmptyResultDataAccess Exception이 발생한다.

```java
//java JUnit Test Code - Update Test

/**
 * @Description : 해당 테스트는 Delete 테스트로
 *                메서드가 실행시 save() 메서드와같이
 *                먼저 Select 로 해당 ID의 데이트를 확인한 후 삭제한다
 *
 *                만약 ID 가없는 값을 삭제를 요청할 경우
 *
 *                org.springframework.dao.EmptyResultDataAccessException 에러가 발생한다.
 * */
@Test
void testDelete() {
    memoRepository.deleteById(100L);
}
```

<h3>7 ) JpaRepository를 사용한 Paging</h3>

- 1 . 페이징 옵션을 넣어줄 <b>Page\<> 객체</b>가 필요하다
- 2 . 해당 Page< T > 객체는 <b>new() 사용해 생성이 불가능</b>하고 <b>PageRequest.of()인 static 메서드</b>를 통해 생성이 가능하다.
- 3 . JpaRepository에서 Page에 사용되는 메서드는 <b>findAll()</b> 이다.

```java
//java JUnit Test Code - Paging  Test

/**
 * @Description : Spring Data JPA 에서 페이징 처리와 정렬은 findAll()이란 메서드를 사용한다
 *
 *                findAll()는 jpaRepository 인터페이스의 상위 PagingAndSort Repository 의 메서드이며
 *                 - 메서드에 전달되는 파라미터는 Pageable 이라는 타입의 객체에 의해 실행되며 해당 메서드로
 *                   쿼리가 작성된다
 *
 *                또한 finaAll()의 return Type 은 Page<T> :: 페이징 시
 *                    , Iterable<T>  :: 정렬 return 시
 *                    이며  리턴 타입을 Page<T>로 지정하는 경우 반드시 파라미터는 Pageable 이어야한다!
 *
 *              -----------------------------------------------------------------------------------
 *
 *              : 페이징 처리에  가장 중요한 존재는 Pageable Interface 이며 해당 Interface는
 *                페이지 처리에 필요한 정보를 전달하는 용도의 타입의 인터페이스 이기 때문에
 *
 *                실제 객체를 생성할 때는 PageRequest 라는 Class 를 사용한다
 *                -  해당 Class 는 protected 로 선언 되어 있기 떄문에 new 를 사용하여 객체변수 생성이 불가능하다
 *                - 객체를 생성하기 위해서는 static Method 인 of()를 이용해야한다
 *                - of()의 Overloading Method
 *                   - of(int page, int size) : 0부터 시작하는 페이지 번화와 개수 [ 정렬 X ]
 *                   - of(int page, int size, Sort direction, String ... props ) : 0부터 시작
 *                                                                                하는 페이지 번화와 개수
 *                                                                                , 정랼의 방향과 정렬의 기준
 *                  - of(int page, int size, Sort sort) : 페이지 번호와 개수, 정렬에 관한 정보
 *
 *               ----------------------------------------------------------------------------------
 *
 *               ✔ 여기서 주의 깊게 볼 부분은 Return Type 인 Page 이다 그 이유는
 *                 - 해당 타입은 단순히 해당 목록만을 가져오는데 그치지 않고 실제 페이지 처리에 필요한 전체 데이터의
 *                   개수를 가져오는 쿼리 역시 같이 처리하기 때문이다
 *                   ( 데이터가 충분하지 않다면 데이터의 개수를 가져오는 쿼리를 실행하지 않는다. )
 *
 * */
@Test
void testPageDefault() {
    //1페이지에 10개씩 ✔ 0페이지가 1페이지 이다!
    Pageable pageable = PageRequest.of(0,10);
    //아래의 findAll()읜 Pageable을 매개변수로 요구 __ 단 그냥 List<>로 받는경우는 다른것도 가능함.
    Page<Memo> result = memoRepository.findAll(pageable);

    System.out.println("----------------------------------------------");

    System.out.println("total Pages : " + result.getTotalPages()    ); // 총 페이지
    System.out.println("Total Count : " + result.getTotalElements() ); //전체 개수
    System.out.println("Page Number : " + result.getNumber()        ); //현재 페이지 번호
    System.out.println("Page Size   : " + result.getSize()          ); //페이지당 데이터 개수
    System.out.println("Has Nest Page ? : " + result.hasNext()      ); //다음페이지 유무
    System.out.println("First Page? : " + result.isFirst()          ); // 시작페이지 여부

    System.out.println("----------------------------------------------");

    System.out.println(result.getContent()); //실제 데이터는 getContent()에 들어있음
}
```

- 정렬 조건 추가 Paging

```java
//java JUnit Test Code - PagingWithSrot  Test

@Test
void testSort() {
    Sort sort1 = Sort.by("SortTargetKey").descending();   // DESC - 내림차순
    Pageable pageable = PageRequest.of(0 , 10 , sort1);  // 위에서 만든 정렬조건을 추가
    Page<Memo> result = memoRepository.findAll(pageable);
    result.getContent().forEach(System.out::println);

    System.out.println("--------------------------------------");

    System.out.println("ASC 오름차순 ascending() 사용");
    Sort sort2 = Sort.by("memoText").ascending();       //ASC 오름차순
    pageable = PageRequest.of(0 , 10 , sort2);
    result = memoRepository.findAll(pageable);
    result.getContent().forEach(System.out::println);

    System.out.println("--------------------------------------");

    System.out.println("정렬 방법 2개 혼합 mno Desc And memoText ASC");
    Sort sortAll = sort1.and(sort2);                  // and를 이용해서 sor1 과  sort2를 이어줌
    pageable = PageRequest.of(0 , 10 , sortAll);
    result = memoRepository.findAll(pageable);
    result.getContent().forEach(System.out::println);

    System.out.println("--------------------------------------");
    //체이닝 방법으로도 가능하다
    pageable = PageRequest.of(0 , 10 , Sort.by("mno").descending()
                      .and(Sort.by("memoTest").ascending()));       // 체이닝으로 해걸
    result = memoRepository.findAll(pageable);
    result.getContent().forEach(System.out:println);
}
```

<br/>
<hr/>

<h3>8 ) QueryMethod</h3>

- <b>메스드의 이름 자체가 Query</b>가 되는 기능이다. 주로 "findBy, getBy..." 로 시작한다.
  <br/> ㄴ> By...는 모두 Select의 일을 하는 keyword이며 개발자는 이중에서 자신이 가독성이 높다고 생각되는 것을 이용하면 된다.
- 추가적인 조건은 "And, or, between, In , IsNull, IsEmpty" 등을 사용하여 추가 가능하다.
- QueryMethod 는 간단한 처리에 적합한 기능으로 보고 사용하면 된다.
- 🎈 주의사항 : &nbsp;Return type은 List, Set, Object등의 여러 타입으로 할 수 있으며 JPA가 데이터를 읽어오고 return type에 맞춰서 데이터를 return해준다.
  <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 단 데이터가 여러개인데 User와 같이 단일 객체로 return하면 오류가 발생한다.

- Select 🔽

```java
//JpaRepository Interface

// Mno 의 경우 PK임으로 단건이기에 타입이 Memo이다.
Memo findByMno(Long mno);

// MemoText의 경우 다건이기에 List<T>로 지정
List<Memo> findByMemoText(String memoText);

/*** First, Top  ***/ //<< -- Last를 구하는 QueryMethod는 없다 oredeyBy를 통해 구해야함!
List<Memo> findFirst1ByMemoText(String memoText);	// 상위 1개의 데이터 return
List<Memo> findTop2ByMemoText(String memoText  );   // 상위 2개의 데이터 return


/*** And, Or ***/
//Where 조건에 And 또는 Or 조건을 넣는것이다.
List<User> findByNameAndEmail(String name, String email); // 이름 과   email 같은 데이터
List<User> findByNameOrEmail(String name, String email);  // 이름 이나 email 같은 데이터


/*** After, Before, GreaterThan, LessThan, Between ***/
/**
 - After, Before, GreaterThan, LessThan은 값 비교를 해주는 keyword이다. [초과 미만]
   ㄴ> After, GreaterThan은 특정 날짜/값 이후(또는 큰것)에 발생한 것을 조회하게 해주는 keyword이다.
   ㄴ> Before, LessThan은 특정 날짜/값 이전(또는 작은것)을 조회하게 해주는 keyword이다.

 - 이상 또는 이하의 조건에는  GreaterThanEqual과 같이 Equal을 붙여주면 된다.

 - 이상 ~이하의 의미를 갖는 Between keyword

 🎈 간단 설명
  - After, Before, GreaterThan, LessThan은 초과 미만을 의미

  - Equal을 붙여주면 이상 이하를 의미 ❌ Equals 아님!!! 이거 떄문에 삽질함..

  - Between은 이상 ~ 이하를 포함하는 데이터들

  -날짜에는 Before, After을 써주자 가독성 때문임!

*/
// CreatedAt이 lastDay이후인 데이터들 return (yesterDay미포함)
List<User> findByCreatedAtAfter (LocalDateTime lastDay);
// input id보다 큰 id를 가진 데이터들을 return (id 미포함)
List<User> findByIdAfter(Long id);
// CreatedAt이 lastDay이후인 데이터들 return (yesterDay미포함)
List<User> findByCreatedAtGreaterThan (LocalDateTime yesterday);
// CreatedAt이 lastDay이후인 데이터들 return (yesterDay포함)
List<User> findByCreatedAtGreaterThanEqual (LocalDateTime yesterday);
// id가 id1이상, id2이하인 데이터들 return
List<User> findByIdBetween(Long id1, Long id2);


/***  is(Not)Null ***/
// - isNull은 해당 값에 Null값이 있는지 체크하는 keyword이다.
List<User> findByIdIsNull();      // Id값이 Null인 데이터들
List<User> findByIdIsNotNull();   // Id값이 Null이 아닌 데이터들


/***  In ***/
// - In절은 query문에서 in절이기 때문에 parameter로 iterater type인 list가 들어가게 된다.
List<User> findByNameIn(List<String> name);


/***  StartingWith, EndingWith, Contains [ Query :: Like 기능 ]***/
List<User> findByNameStartingWith(String name);  // like %name
List<User> findByNameEndingWith(String name);    // like name%
List<User> findByNameContains(String name);      // like %name%
List<User> findByNameLike(String name);          // like %name%

/***  Sort ***/
List<User> findByNameOrderByIdDesc(String name);
// findBy "TargetColumn" "Between" "OrderBy" "TargetColumn" "Desc"
List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

```

- Select With Pageable [ QueryMethod에 Pageable 을 매개변수로 받아 사용 방법 ]🔽

```java
//JpaRepository Interface

/**
 * @Description  : 위의 메서드의 경우 이름도 길고 혼동하기 쉽다
 *                 메서드 쿼리는 다행히도 Pageable 를 파라미터로 받아
 *                 사용이 가능하다
 *
 *                 ✔ Pageable 에서 정렬하여 사용!
 *
 *                 🎈 Pageable 을 사용 했으므로 Return 값은 Page가 됨!
 * */
Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);


////////////////////////////////////////////////////////////////////////////////

//JUnit Test Code - queryMethod With Pageable
Pageable pageable = PageRequest.of(0                // 페이지 번호
                                  , 10              // 페이지 개수
                                  , Sort.by("mno").descending()); //정렬 조건

Page<Memo> result = memoRepository.findByMnoBetween(40L,50L,pageable);

```

- Delete🔽

```java
//JpaRepository Interface

/***
 * @Description  : 삭제 매서드 쿼리 __ ">" mno 가 10미만 데이터 삭제
 * */
@Transactional
void deleteMemoByMnoLessThan(Long num);

///////////////////////////////////////////////////////////////////////

//JUnit Test Code - queryMethod With Delete
/**
 * @Description  : 🎈 주의사항으로 QueryMethod + Delete 테스트 시  @Transactional
 *                    을 사용하지 않으면 에러가 밸상한다.
 *                    :::[ interface의 해당 메서드에 붙여 사용해도 괜찮음!! 선택사항임]
 *                    Error Msg = No EntityManager with actual transaction available
 *                                for current thread - cannot reliably process 'remove' call;
 *                                nested exception is javax.persistence
 *                                TransactionRequiredException
 *
 *                  - @Commit ?
 *                    최종결과를 커밋하기 위해 사용된다. 해당 어노테이션을 작성하지 않아도 Error는
 *                    발생하지 않으나 테스트 코드의 deleteBy 는 기본적으로 롤백처리되어 DB에 반영되지가 않는다.
 *
 *                 ✔  해당 deleteBy는 실제 개발에서는 많이 사용되지 않는데
 *                     그 이유는 삭제쿼리를 한번에 날리는 것이 아닌
 *                    각 엔티티 객체를 하나씩 삭제하기 떄문에 비효일 적이기 때문이다.
 *                    => 따라서 해당 deleteBy 쿼리 메서드보다는 @Query 어노테이션 기능을 사용해서
 *                        비효율적인 부분을 개선해서 사용해야한다.
 *
 *
 * result Query : Hibernate:
 *               select
 *                   memo0_.mno as mno1_0_,
 *                   memo0_.memo_text as memo_tex2_0_
 *               from
 *                   tbl_memo memo0_
 *               where
 *                   memo0_.mno < ?
 *           Hibernate:
 *               delete
 *               from
 *                   tbl_memo
 *               where
 *                   mno=?
 *           Hibernate:
 *               delete
 *               from
 *                   tbl_memo
 *               where
 *                   mno=?
 *           Hibernate:
 *               delete
 *               from
 *                   tbl_memo
 *               where
 *                   mno=?
 * */
@Commit
@Transactional
@Test
void testDeleteQueryMethods() {
    memoRepository.deleteMemoByMnoLessThan(9L);
}

```

<br/>
<hr/>

<h3>9 ) @Query 어노테이션 [ JPQL ]</h3>

- Spring Data JPA가 제공하는 Query Metho는 검색과 같은 기능을 작성할 때 편리하지만
  <br/> 개발 시 <b>Join 이나 복잡한 조건을 처리해야 할 경우 Method명 또한 복잡해지고 어려워집니다.</b>
  <br/>때문에 일반적으로 <b>간단한 처리의 경우 Query Method를 사용</b>하고
  <br/> <b>대부분의 경우 @Query[ JPQL ] 또는 QueryDSL을 사용</b>하여 개발한다.
- 메서드의 이름과 상관없이 메서드에 추가한 @Query 어노테이션을 통해 처리가 가능하다
- @Query [ JPQL ] 의 Value는 객체지향 쿼리 구문을 사용한다.
- AVG(), COUNT(), GROUP BY, ORDER BY 등 집계함수, 정렬 Query 문이 사용 가능하다.
- ✅ 알아둬야 할 것 !
  <br/> ㄴ> @Modifying 이란❔
  <br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - &nbsp;@Query 어노테이션(JPQL Query, Native Query)을 통해 작성된
  <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INSERT, UPDATE, DELETE (SELECT 제외) 쿼리에서 사용되는 어노테이션이다.
  <br/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - &nbsp;기본적으로 JpaRepository에서 제공하는 Method 혹은 QueryMethod는 적용되지 않는다.
- @Query의 장점중 하나는 QueryMethod의 경우 Entity안의 데이터만 추출하지만 @Query의 경우 현재 필요한 데이터 뿐만이
  <br/> &nbsp;아니고 <b>추가적인 데이터를 Object[]의 형태로 선별적으로 추출이 가능하다.</b>
- 경우에따라 JOIN 이나 GROUP BY등을 이용하는 경우에도 Entity에 속성이 없을 경우 유용하게 사용가능.
- nativeQuery = boolean 으로 <b>Navtive Query로도 사용이 가능</b>하다

```java
//JpaRepository Interface

// 여기서 주의 깊게 보아야 하는 부분은 Table명은 tbl_memo지만 Entity Class 명으로 작성 되었다.
// 컬럼명 또한 맴버변수 명으로 사용!
@Query("select m from Memo m order by m.mno desc")
List<Memo> getListDesc();

 /**
 *  Error - org.hibernate.hql.internal.QueryExecutionRequestException
 *          : Not supported for DML operations <<  @Modifyin 없을 경우
 *
 *        - nested exception is javax.persistence.TransactionRequiredException
 *          : Executing an update/delete query <<  @Transactional 없을 경우
 *
 *  - @Modifying, @Transactional 경우 Repositroy에 작성해도 되고
 *   테스트 하려는 Method 에 작성해도 된다.
 *
 *  - 간편함을 생각하면 Repository에 작성하는것이 좋아보임
 * */
@Modifying
@Transactional
@Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
int updateMemoText(Long mno, String memoText);

/**
 * @Description  : 위의 방법을 사용하면 변수가 여러개일 경우 불편하다
 *                " # "  를 사용해서 객체 변수로 전달 받아 사용이 가능하다
 *
 *                 🎈 주의사항 - 1) #{param.mno} 가 아니라 #{#param.mno} 이다  #이 2개임!!
 *                              2) :#{#..} 앞에 " : " <<- rk 가 있음 !
 *                              3) 굳이  변수명이 같다면  @Param("??")은 없어도 상관없음
 * */
@Transactional
@Modifying
@Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
int updateMemoTestWithObj(@Param("param") Memo memo);

/**
 * @Description : @Query 어노테이션에서도
 *                Pageable를 Parameter로 받아 페이징 처리와 정렬에 대한 부분을
 *                Pageable로 처리가 가능하다.
 *
 *                🎈 주의사항 - countQuery  = "Query문"을 작성하여 카운트를 처리를 해주면 페이징 시
 *                             카운트를 해주지만 안써줘도 자동으로 쿼리가 작성된다.
 *                             필수는 아니며 다음에 배울 Native Query를 사용시는 필수이다.
 *
 *                             단순히 데이터 뿐 아니라 페이지 처리에 필요한 모든 내용을 처리합니다.
 *
 *                           설명 : 화면에 페이징 처리를 위해서는 전체 데이터의 수를 파악할 필요가 있습니다.
 *                                  이를 이용해서 전체 몇 개의 페이지로 만들어지는지
 *                                  이전/다음 페이지의 정보 들도 같이 처리합니다.
 *                                  <strong>예를 들어 데이터가 부족하면 카운트 쿼리를 실행하지 않습니다.</strong>
 * */
@Query(value = "select m from Memo m where m.mno > :mno"
        , countQuery = "select count(m) from Memo m where m.mno > :mno"
)
Page<Memo> getListWithQuery(Long mno, Pageable pageable);

/**
 * @Description  : @Query 어노테이션의 장점은 return 타입을 Object[]로 사용이 가능하다는 점이다!
 *
 *                하위 value = "query"를 확인해보면 내가 만든 Memo entity class 에는 날짜 타입의
 *                컬럼을 지정해주지 않았지만  CURRENT_CATE, CURRENT_TIME, CURRENT_TIMESTAMP
 *                를 사용하여 현재 데이터베이스의 시간을 구할 수 있다.
 *
 *                이처럼 엔티티에 타입이 존재하지 않을 경우 사용이 가능하다
 *
 *                🎈 쿼리 메서드에서는 Object 타입을 반환 불가능함!! entity class 로 지정한 것만 가능
 *
 * */
@Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno"
        , countQuery = "select count(m) from Memo m where m.mno > : mno"
)
Page<Object[]> getListWithQueryObject(@Param("mno") Long mno, Pageable pageable);

/**
 * @Description : 데이터베이스이 고유의 SQL문을 그대로 활용하는 방법
 *
 *                 JPA 자체의 데이터베이스에 독립적으로 구현 가능하다는 장점을 잃어버리긴 하지만
 *
 *                 경우에 따라서 복잡한 JOIN 구문 등을 처리하기 위해 어쩔수 없는 경우에 선택 사용함
 *
 *                 🎈 주의사항 : nativeQuery 의 경우 table 명은 Database 의 Table 명을 따라간다
 * */
@Query(value = "select * from tbl_memo where mno > 0"
        , nativeQuery = true //네이티브 Query를 사용하겠다!
)
List<Object[]> getNativeResult();

```

<br/>
<hr/>

<h3>10 ) Docker를 사용한 MariaDB 설치 및 연결</h3>

- 1 . Docker를 설치한다.
- 2 . 설치가 완료 되면 Terminal를 통해 설치를 확인한다.

```properties
##Docker Version Chehck

docker -v

docker --version
```
- 3 . 설치가 잘되어 버전이 확인 된다면 Docker를 통해 원하는 버전의 <strong> MariaDB Image를</strong> 가져온다.
```properties
##Docker Get Images

## 최신 버전을 가져오는 방법과 내가 버전을
## 지정해서 가져오는 방법이 존재헌다.

# 최신 버전의 MaraiaDB Image를 가져옴
docker pull mariadb

# 내가 지정한 버전의 MaraiDB를 가져옴
docker pull maraidb:10.6
```
- 4 . Docker를 통해 Image를 가동 시킨다.
```properties
##Docker Image run

# -p ??:?? (Port)지정 localPort: ocerPoort
# -e ??    환경변수 설정 (Password)를 설정했음
# -d ??    Background로 실행 하겠다 
docker run -p 3306:3306 --name mariadb -e MARIADB_ROOT_PASSWORD=123 -d mariadb:10.6
```
- 5 . 실행된 이미지에 접근
```properties
##Docker Connect Mariadb

docker exec -it mariadb /bin/bash
```
- 6 . DB 로그인 및  Database 생성 접근 
```properties
#Mariadb Login and access databases

#접근이 되었다면 확인
mariadb --verseion;

#Root 계정으로 Login
mariadb -u root -p;

#Password는 4번에서 설정한 123이다.
123

#databases 확인
shoe databases;

#database 생성
create database ???;

#database 선택
use ??;

```