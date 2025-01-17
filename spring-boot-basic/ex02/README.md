# JPA (QueryMethod, JQPL, Paging) 

## ORM [ 객체 관계 매핑 (Object–relational mapping) ]❔

- **Class**와 **RDB**의 테이블을 **매핑**한다는 의미이다.  
  - 기술적으로는 애플리케이션의 객체를 RDB 테이블에 자동으로 **영속화**해주는 것을 말한다.
  - **객체지향 패러다임을 관계형 데이터베이스에 보존하는 기술** 
- 쉽게 말해, **Class(객체)의 구조로 Table을 생성해 사용하는 것**


### Class Entity 와 Table Row

```properties
# ℹ️ Class와 Table은 구조적으로 유사 함, 인스턴스(Class)와 Row(튜플) 또한 유사 함 
```

#### 차이:
- **Class**: **데이터 + 행위(메서드)**를 포함한다.
  - 객체지향에서는 인스턴스를 생성하여 데이터와 행위를 포함한 공간에 데이터를 보관한다.
- **Row**: **데이터만**을 포함한다.
  - 하나의 Row에 **데이터만** 저장한다.

### ORM의 장점

- **SQL 문이 아닌 Method**를 통해 DB를 조작하므로, 개발자는 객체 모델을 이용해 비즈니스 로직 구성에만 집중할 수 있다.
- **객체 지향적인 코드 작성**이 가능하다. 객체 지향적 접근에만 집중할 수 있어 생산성이 증가한다.
- 매핑하려는 정보가 **Class(Entity)**로 명시되어 있어 ERD 의존도를 낮출 수 있으며, 유지보수와 리팩토링에 유리하다.
- DB의 종류에 의존적이지 않다. 예: MySQL에서 Oracle로 변경 시 SQL문을 사용하지 않아도 수정이 필요 없다.

### ORM의 단점

- 테이블 간 관계가 복잡하면 설계 오류로 인해 **속도 저하** 및 일관성 문제가 발생하며, 관계 설정이 어려워질 수 있다.
- 복잡하거나 성능에 비효율적인 Query가 생성되는 경우가 있다. (예: **N+1 문제**) 
  

<hr/>

## JPA(Java Persistence API)❔

- ORM을 Java언어에 맞게 사용하는 인터페이스 모음
  - ORM 이 좀 더 **상위 개념**이고 ORM을 기술 표준에 맞게 사용하는 인터페이스 개념으로 볼수 있다
- JPA는 인터페이스 이기 때문에 Hibernate, OpenJPA 등이 JPA를 구현
- Spring Framework에서 **"Hibernate"가 대표적**

<hr/>

## JPA, DB 추가 및 DB접속 정보 설정

```properties
# ✅  주의사항 : JPA 사용 시   Database connection 정보 미 기입시 에러 발생
```

### DB 설정
- Database Dependency 추가
  - DB query Log를 이쁘게 보고 싶을 경우 **p6spy** 추가 설정 필요함

```groovy
//build.gradle
dependencies {
    
    implementation group: 'org.mariadb.jdbc', name: 'mariadb-java-client'

}

```

#### DB connection
```properties
#application.properties

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3310/bootex
spring.datasource.username=root
spring.datasource.password=123
```

###  JPA 설정
- 운영 환경에 맞는 설정 값을 지정해주는 것이 중요

```properties
## 기본적으로 Database 설정이 되어 있어야 한다!

######################
#   JPA setting      #
######################

# 프로젝트 실행 시 에 자동으로 DDL을 생성할 것인지 결정하는 설정이다.
# 설정값 : create         - (Local 환경) 프로젝트 실행 시 매번 테이블을 생성한다.[ create 옵션은 해당하는 테이블이 있으면 DROP하고 새로 만들어 버린다 ]
#        update         - (Dev 환경)   프로젝트 실행 시 테이블이 없으면 만들어주고 있는데 수정이 필요할 경우 alter 해준다. [ 운영DB에서는 사용하면 안됨 ]
#        create-drop    - (Local 환경) create와 비슷하지만 서버 종료시점에 테이블 DROP 시켜버린다.
#        validate       - (Prod 환경)  엔티티와 테이블이 정상 매핑되었는지만 확인한다.                                                    
#        none           - (Prod 환경)  기존 있는 것을 사용만함 - 초기 Table 설정 필요
spring.jpa.hibernate.ddl-auto = update

# 실제 JPA의 구현체인 Hibernate가 동작하면서 발생하는 SQL을 포맷팅 하여 콘솔에 출력해준다.
spring.jpa.properties.hibernate.format_sql = true

# JPA 처리 시에 발생하는 SQL을 보여줄지 설정
spring.jpa.show-sql = true

```

### Entity Class
```properties
# ℹ️ @EqualsAndHashCode : 엔티티 동등성 비교를 정확하고 간편하게 구현할 수 있는 방법이다.
#                          하지만 연관 관계 필드를 제외하고, 식별자 필드만 포함하도록 설정하여 의도치 않은 문제를 방지해야 한다
#
# ℹ️ @NoArgsConstructor(access = AccessLevel.PROTECTED) : 엔티티 클래스를 인스턴스화할 때 프록시 기술을 사용하며,
#                                                         이를 위해 기본 생성자가 필요한데 접근 Level로 설정하여 불필요한 객체 생성을 막는것
```
- #### @EqualsAndHashCode
  - 직접 엔티티 클래스를 비교하는 것이 아니라 프록시 객체를 비교할 수 있으므로 equals()를 올바르게 작성하는 것이 중요
    - @EqualsAndHashCode는 이를 올바르게 처리하는 데 도움이 됩니다.
  - tackOverFlow가 생길 수 있는 일을 미연에 방지가 가능하다.
- `@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)`
  - `onlyExplicitlyIncluded`를 **true**로 설정 하여 원하는 필드에 `@EqualsAndHashCode.Include` 설정을 통해 hasCode 생성 가능
  - `callSuper=false` 설정 이유는 불필요한 부모 호출을 막기 위함이다.
- 일반적으로 `@EqualsAndHashCode.Include`에 **id만 포함**
- 불필요한 계산 방지
  - 특히 객체가 많은 필드를 가지고 있는 경우, 필요없는 계산을 줄이는 것은 성능에 긍정적인 영향을 미칠 수 있음
- 🤯 헷갈렸던 부분
  - Query에서도 `@EqualsAndHashCode(onlyExplicitlyIncluded = true) ` 대상 미지정시 영향이 있는가?
    - 해당 옵션은 객체간의 비교에만 영향이 있다.
      - Ex) `Member m1 = r.findById(1); 와 Member m2 = r.findById(1);` 비교 하는 경우에만 영향이 있다

- ####  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  - 엔티티에서 기본 생성자를 제공해야 하지만, 외부에서의 무분별한 사용을 막고자 할 때 사용
  - JPA 프록시 생성 지원, 객체 생성 제어, 캡슐화 강화 등의 이유로 활용
  - JPA는 리플렉션을 사용하여 엔티티 인스턴스를 생성할 때 매개변수가 없는 기본 생성자가 필요하다.
  - 개발자가 명시적으로 기본 생성자를 제공하지 않으면, 컴파일러는 자동으로 생성 한다
    - 개발자가 명시적으로 기본 생성자를 제공하지 않으면, 컴파일러는 자동으로 생성합니다.
  - Builder Pattern을 사용할 경우에도 생성자 메서드 명시가 필요하다.
  - `@NoArgsConstructor(access = AccessLevel.PROTECTED)`로 접근제어를 하는 이유?
    - Entity 연관 관계에서 지연 로딩의 경우에는 실제 Entity가 아닌 ⭐️`proxy`객체를 통해서 조회를 한다.
      - 프록시 객체를 사용하기 위해서 JPA 구현체는, 실제 엔티티의 기본 생성자를 통해 프록시 객체를 생성하는데, 이 때 접근 권한이 `private`이면 **`proxy` 객체를 생성할 수 없기 떄문이다.**
        - 👉 단! 즉시로딩으로 구현하게 되면, 접근 권한과 상관없이 `proxy`객체가 아닌 실제 `Entity`를 생성하므로 문제가 생기지 않는다

```java
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
 *                , @NoArgsConstructor 
 *
 *             : 주의사항 - @Data , @Setter 를 사용하지 않는 이유는 Entity Class의 값은 직접적인 Class의 수정이 적을 수록 좋음
 *
 * */
@Entity
@Table(name = "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper=false)
public class Memo {

    @Comment("PK")
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Comment("메모")
    @Column(length = 200, nullable = false)
    private String memoText;

}
```

#### 복합키 처리 방법
```properties
# ℹ️ 복합키 처리 방법 `@EmbeddedId`, `@IdClass`가 있으며, 진행하는 프로젝트 스타일에 맞춰 사용하는 것이 중요하다 
```

##### 비교
- `@EmbeddedId` : 좀 더 객체 지향에 가깝다
  - Query : `select p.id.id1, p.id.i2 from Parent p`
- `@IdClass` : 좀 더 RDBMS에 가깝다
  - Query : `select p.i1, p.id2 from Parent p`

##### 사용법
- #### `@EmbeddedId` 
  - PK Class
    - Class에 `@Embeddable` 지정
    - 기본 생성자 필수
    - `Serializable` 인터페이스 구현
    - equals, hashCode 구현(`@EqualsAndHashCode`) - 식별자로 사용하기 위함
  - Entity Class
    - 해당 PK 필드 `@EmbeddedId` 적용
    - 해당 PK Class 자체에서 equals, hashCode 사용중 이므로 `@EqualsAndHashCode` 불필요
  - 예시
    ```java
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    @Builder
    public class MemberPK implements Serializable {
        private String name;
        private Integer SSID;
    }
      
    ///////////////////////////////////////////////////////
      
    @Entity
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public class Member {
      
        @EmbeddedId
        private MemberPK id;
      
        // code ...
    }
    ```
- #### `@IdClass`
  - PK Class
    - 기본 생성자 필수
    - `Serializable` 인터페이스 구현
    - equals, hashCode 구현(`@EqualsAndHashCode`) - 식별자로 사용하기 위함
  - Entity Class
    - `@IdClass(만들어진 PK클래스.class)` 지정 필요
    - 각각의 필드를 재정의 하면된다.
      - `@Id`으로 PK 다중 지정이 가능해짐
  - 예시
    ```java
      
    @Entity
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public class GrandParent {
        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long grandParentId;
      
        private String grandParentName;
    }
      
    ///////////////////////////////////////////////////////
      
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public class ParentPK implements Serializable {
      
        @EqualsAndHashCode.Include
        private Long parentId;
      
        @EqualsAndHashCode.Include
        private Long grandParent;
      
    }
      
    ///////////////////////////////////////////////////////
      
    @IdClass(ParentPK.class)
    @Entity
    public class Parent {
          
        @Id
        @Column(name= "parent_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long parentId;
      
        @Id
        @ManyToOne
        @JoinColumn(name = "grand_parent_id")
        private GrandParent grandParent;
      
    }
    ```



### JpaRepository 

- Spring Data JPA 는 구현 체인 Hibernate를 이용하기 위한 여러가지 API를 제공
  - 그 중에서 가장 대표적으로 사용되는것이 **JpaRepository Interface** 
- Spring Data JPA에는 여러 종류의 인터페이스 기능을 통해 JPA 관련 작업을 별도의 코드 없이도 진행이 가능하도록 지원
- 간단한 CRUD 작업 혹은 페이징, 정렬 처리도 인터페이스의 메서드를 호출하여 처리가 가능하고 필요에 따라서 상속 구조로 추가적인 기능도 제공
- ✅중요완
  - Repository는 <b>Interface</b> 임
  - <b>JpaRepository</b>를 상속 받아야 기능이 사용 가능
    - <b>extends JpaRepository</b> 상속을 받는 것 만으로도 Bean Conatainer에 등록 대상이 된다.

```java
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

### JpaRepository를 사용한 CRUD
- ✨ Transaction 어노테이션을 잘 사용 해주자 효율적인 Transaction 처리르 성능 향상 및 DB 이중화때 용의함
  - 단순 조회 일 경우 
    - `@Transactional(readOnly = true)`
  - 그외
    - `@Transactional`

```java
@Service
@RequiredArgsConstructor
public class MemberServiceImpl {

    private final MemberRepository memberRepository;

    // Create
    @Transactional
    public Member registerMember(String accountId, int age, String name) {
        Member member = Member.builder()
                .accountId(accountId)
                .age(age)
                .name(name)
                .build();
        return memberRepository.save(member);
    }

    // Read List
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Read 
    @Transactional(readOnly = true)
    public Member getMemberByAccountId(String accountId) {
        return memberRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    // Update
    @Transactional
    public Member updateMember(Long id, int newAge, String newName) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        member.setAge(newAge);
        member.setName(newName);
        return memberRepository.save(member);
    }

    // Delete
    @Transactional
    public void deleteMember(Long id) {
      memberRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Member not found"));
        memberRepository.deleteById(id);
    }
}
```


### JpaRepository를 사용한 Paging

- 페이징 파라미터 값은 Pageable 객체를 사용 
  - `Pageable pageable = PageRequest.of(0,10);`
    - PageRequest.of( 현재 페이지 번호, 목록 개수 , 정렬 옵션 );
- JpaRepository에서 Page에 사용되는 메서드는 <b>findAll()</b> 이다.
- 반환 형식은 Page< T > 형태로 반환
  - 해당 객체 내에는 페이징에대한 모든 정보가 객채 형식으로 들어가 있음
    - 총 페이지
    - 전체 개수
    - 현재 페이지 번호
    - 페이지당 데이터 개수
    - 다음페이지 유무
    - 시작페이지 여부

#### 정렬 조건 없이
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
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public Page<Member> getPagedMembers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    // 페이징된 결과 조회
    Page<Member> result = memberRepository.findAll(pageable);

    // 페이징 관련 정보 로그 출력
    log.info("Total Pages       : {}", result.getTotalPages());
    log.info("Total Elements    : {}", result.getTotalElements());
    log.info("Current Page      : {}", result.getNumber());
    log.info("Page Size         : {}", result.getSize());
    log.info("Has Next Page?    : {}", result.hasNext());
    log.info("Is First Page?    : {}", result.isFirst());
    log.info("Paged Members     : {}", result.getContent());

    return result;
  }
}
```

### 정렬 조건 추가 

```java
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl {

  private final MemberRepository memberRepository;

  public Page<Member> getPagedMembers(int page, int size, String sortColum, boolean isAscending) {
    // 정렬 조건 설정
    Sort sort = isAscending ? Sort.by(sortColum).ascending() : Sort.by(sortColum).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    /** 추가 조건 생성 */
    // Sort addSort = sort.and(sort2);  -> // and를 이용해서 추가 정렬 가능                  
    // pageable = PageRequest.of(0 , 10 , Sort.by("mno").descending()
    //                .and(Sort.by("memoTest").ascending()));   ->    // 체이닝 방식으로 추가
    
    // 페이징된 결과 조회
    Page<Member> result = memberRepository.findAll(pageable);

    // 페이징 관련 정보 로그 출력
    log.info("Total Pages       : {}", result.getTotalPages());
    log.info("Total Elements    : {}", result.getTotalElements());
    log.info("Current Page      : {}", result.getNumber());
    log.info("Page Size         : {}", result.getSize());
    log.info("Has Next Page?    : {}", result.hasNext());
    log.info("Is First Page?    : {}", result.isFirst());
    log.info("Paged Members     : {}", result.getContent());

    return result;
  }
}
```

## QueryMethod

- <b>메스드의 이름 자체가 Query</b>가 되는 기능이다
- select는 `findBy, getBy` 로 시작한다.
  - 두가지 모두 Select의 일을 하는 keyword이며 개발자는 이중에서 자신이 가독성이 높다고 생각되는 것을 이용하면 된다.
- where 조건은 `And, or, between, In , IsNull, IsEmpty`을 사용
- QueryMethod 는 **간단한 처리에 적합한** 하다
- 🎈 주의사항 : 반환 데이터가 복수인데 User와 같이 단일 객체로 return 할 경우 RuntimeException 발생

### Select 🔽

```java
public interface MemoRepository extends JpaRepository<Memo,Long>{
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
}
```

### Select - paging 🔽
```java
public interface MemoRepository extends JpaRepository<Memo,Long>{
  /**
   * @Description  : 위의 메서드의 경우 이름도 길고 혼동하기 쉽다
   *                 메서드 쿼리는 다행히도 Pageable 를 파라미터로 받아
   *                 사용이 가능하다
   *
   *                 ✔ Pageable 에서 정렬하여 사용!
   *
   *                 🎈 Pageable 을 사용 했으므로 Return 값은 Page가 됨!
   * */
  // 입력 받은 mno 사이 목록 페이징
  Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
}
```

### Delete🔽

```properties
# ℹ️ QueryMethod + Delete 테스트 시  @Transactional을 사용하지 않으면 에러가 밸상한다.
#
# deleteBy는 실제 개발에서는 많이 사용되지 않음
# - 삭제쿼리를 한번에 날리는 것이 아닌 각 엔티티 객체를 하나씩 삭제하기 떄문에 비효일 적이기 때문이다.
#   => 따라서 해당 deleteBy 쿼리 메서드보다는 @Query 어노테이션 기능을 사용해서 비효율적인 부분을 개선해서 사용해야한다.
```

```java
public interface MemoRepository extends JpaRepository<Memo,Long>{
  /**
   * @Description  : 🎈 주의사항으로 
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
   *
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
  // 삭제 매서드 쿼리 __ ">" mno 가 10미만 데이터 삭제
  @Transactional
  void deleteMemoByMnoLessThan(Long num);    
}
```

## JPQL (Java Persistence Query Language)

```properties
# ℹ️ JPQL은 **Java Persistence API (JPA)**에서 사용하는 객체 지향 쿼리 언어로, 엔티티(Entity)를 대상으로 하는 쿼리를 작성할 때 사용됩니다. 
#   SQL과 비슷하지만, JPQL은 JPA의 엔티티 객체를 대상으로 작성함
#
# `nativeQuery = boolean` 설정으로 <b>Navtive Query</b> 사용 가능
```

### 장점
- Join 이나 복잡한 조건을 처리해야 할 경우 편리함
  - JpaRepository을 사용할 경우 Join 이나 조건이 복잡할 경우 Method명 또한 복잡해지고 어려지짐
  - 일반적으로 **간단한 처리**의 경우 **Query Method**를 사용
  - 대부분의 경우 JPQL 또는 QueryDSL을 사용
- 객체 지향적 접근:
  - 객체 중심의 쿼리 작성이 가능하여 코드의 일관성을 유지
- AVG(), COUNT(), GROUP BY, ORDER BY 등 집계함수, 정렬 Query 문이 사용 가능

### 단점
- 복잡한 쿼리 작성의 한계
  - 복잡한 연산이나 고성능이 요구되는 쿼리(예: 대량 데이터 처리, 특정 데이터베이스 고유 함수 사용)는 JPQL로 처리하기 어렵습니다.
- 데이터베이스 기능 제한
  - 데이터베이스 고유 기능(예: 특정 함수나 인덱스 힌트 사용, 윈도우 함수 등)을 직접 활용하기 어려움
  - JPQL은 표준 SQL의 범위 안에서 동작하므로 데이터베이스 종속적인 고급 기능은 지원하지 않음
- Lazy Loading 관련 문제
  - JPQL로 작성한 쿼리가 연관 엔티티를 명시적으로 로드하지 않을 경우, Lazy Loading으로 인해 성능 문제가 발생할 수 있음
    - 예: 연관된 데이터가 필요한데 JPQL에서 JOIN을 명시하지 않으면, 연관 데이터 조회 시마다 추가 쿼리가 실행 **(N+1 문제 발생 가능)**

### 주의 사항
- SQL과 혼동 금지
  - JPQL은 SQL처럼 보이지만, 실제로는 객체 지향 언어입니다. SQL에서 사용하는 테이블 이름과 컬럼을 사용하면 오류가 발생
- Lazy Loading:
  - 관련 엔티티를 가져오려면 명시적으로 JOIN을 사용해야 합니다.
- 성능 튜닝:
  - 복잡한 쿼리는 성능 문제를 유발할 수 있으므로 필요한 경우 네이티브 SQL을 사용하는 것도 고려해야 합니다.
- 데이터 변경 시 어노테이션 사용 필요
  - `@Modifying`, `@Transactional`  사용 필요
    - 기본적으로 JpaRepository에서 제공하는 Method 혹은 QueryMethod는 **필요하지 않음**
- Join을 통해 가져온 데이터는 `Object[]`를 사용해서 반환 값 사용

```java
public interface MemoRepository extends JpaRepository<Memo, Long> {
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
}
```