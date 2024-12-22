# Thymeleaf ,Spring MVC

## 1 ) Thymeleaf 란❔

- Spring에서 사용 가능한 화면이다
- JSP와 유시하가 ${}를 별도 처리 없이 이용이 가능하다.
- Model에 담긴 객체를 Javascript로 처리하기 간편하다.
- 개발도구를 이용할때 .html 파일로 생성하는데 문제가 없고 별도 확장자를 사용하지 않아도된다.
- Thymeleaf도 JSP 와 마찬가지로 서버에서 결과를 만들어서 화면에 전송한다 !
  - 만들어진 파일 -> 빌드 시 -> 내부 로직에 맞게 HTML로 재파싱 후 화면에 송출 <b>[tmp0] 파일을 보면 이해가 됨.</b>

### 설정 🔽

- build.gradle
```gradle
dependencies {
  //thymeleaf를 추가해준다.
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
}
```

- application.properties
```properties
## 스프링부트의 Thymeleaf 템플릿 결과는 캐싱하는 것이 디폴트 값이다.
## 즉, 개발할 때 Thymeleaf를 수정하고 브라우저를 새로고침하면 바로 반영이 되지 않는다.
## 따라서 개발을 할 때에는 false로 해 주는 것이 재시작  없이 새로고침만으로 반영되게 하는 것이 편하다.
spring.thymeleaf.cache=false
```


## 2 ) Thymeleaf를 사용해 데이터 출력 [ 반복문 및 조건문]

### Controller🔽

```java
@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {
  //요청 URL : localhost:8080/sample/ex02
  @GetMapping({"/ex2","/이런식으로", "/복수의 URL을 한개의 Method에서 처리도 가능함."})
  public void exModel(Model model){
    List<SampleDTO> list = IntStream.rangeClosed(1,20)
            .asLongStream().mapToObj(i->{ //  Long -> Map 변환
              return SampleDTO.builder()
                      .sno(i)
                      .first("First" + i)
                      .last("last" + i)
                      .regTime(LocalDateTime.now())
                      .build();
            }).collect(Collectors.toList());          
    model.addAttribute("list" ,  list);
  }   
}
```

### View🔽

- view 파일의 Default 경로는 <b>src -> main -> resources -> templates -> "URL 경로에 맞는 Dir"</b>이다.
- 경로를 변경하려면 <b>application.properties</b> 에서 변경이 가능하다.
- 정적 파일[js, css , img 등등..]의 위치는 <b>src -> main -> resources -> static </b>이다.

```html
<!-- Html [thymeleaf] -->

<!-- File  위치 : templates/sample/ex02.html -->
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
    <style>
      .target {
        color: red;
      }
    </style>
  </head>
  <body>
    <h1 th:text="${'I am ex2 get Model Data and Processing Loop Logic'}"></h1>
    <!--
        - ${list} __>  Service 단에서 Model 로 넘긴 데이터

        - th : each = " 각각의 index의 data : Loop Target "

        -  [[  내가  ]] th:each 에서 나온 각각의 Index별 데이터 를 표현 하는 방식
       -->
    <h2>th:each 를 사용하여 Model 로 받아온 데이터 표출 --> th:text 사용❌</h2>
    <p>th:each="dto : ${list}"</p>
    <ul>
      <li th:each="dto : ${list}">[[${dto}]]</li>
    </ul>

    <h2>each 에 변수명을 추가해서 사용해서 각각의 index 번호를 알수있다.</h2>
    <p>th:each="dto , indexNum : ${list}"</p>
    <ul>
      <li th:each="dto , indexNum : ${list}">
        인덱스 번호 :: [[${indexNum.index}]] ---> [[${dto}]]
      </li>
    </ul>

    <h2>조건문 사용 - 5의 배수</h2>
    <p>th:each="dto , indexNum : ${list}" th:if="${dto.sno % == 0}"</p>
    <ul>
      <li th:each="dto , indexNum : ${list}" th:if="${dto.sno % 5 == 0}">
        인덱스 번호 :: [[${indexNum.index}]] ---> [[${dto}]]
      </li>
    </ul>

    <h2>조건문 사용 - if~else</h2>
    <p>th:if = "${dto.sno % 5 == 0}" th:text = "${'---------' + dto.sno}"</p>
    <p>
      th:unless = "${dto.sno % 5 == 0}" th:text = "${'---------' + dto.first}"
    </p>
    <ul>
      <li th:each="dto , indexNum : ${list}">
        <span
          th:if="${dto.sno % 5 == 0}"
          th:text="${'---------' + dto.sno}"
        ></span>
        <span
          th:unless="${dto.sno % 5 == 0}"
          th:text="${'---------' + dto.first}"
        ></span>
      </li>
    </ul>

    <h2>
      삼항 연산자 :: 특이하게도 th 에서는 true 일경우만 해줘도 Error가 없음
    </h2>
    <p>- 단 else 문을 띄어넘는게 아니라 공백값이 들어가 나온다!</p>
    <p>
      th:each="dto , indexNum : ${list}" th:text="${dto.sno % 5 == 0 } ?
      ${dto.sno}"
    </p>
    <ul>
      <li
        th:each="dto , indexNum : ${list}"
        th:text="${dto.sno % 5 == 0 } ? ${dto.sno}"
      ></li>
    </ul>

    <h2>삼항 연산자 :: 일반 비번 boolean ? true : false</h2>
    <p>
      th:each="dto , indexNum : ${list}" th:text="${dto.sno % 5 == 0 } ?
      ${dto.sno} : ${dto.first}"
    </p>
    <ul>
      <li
        th:each="dto , indexNum : ${list}"
        th:text="${dto.sno % 5 == 0 } ? ${dto.sno} : ${dto.first}"
      ></li>
    </ul>

    <h2>
      조건에 맞게 addClass __> th:class 는 삼항식 false 일 경우를 써줘야함
    </h2>
    <p>
      th:each="dto , indexNum : ${list}" th:class = "${dto.sno % 5 == 0 ?
      'target' : ''}"/p>
    </p>
    <ul>
      <li
        th:each="dto , indexNum : ${list}"
        th:class="${dto.sno % 5 == 0 ? 'target' : ''}"
      >
        [[${dto}]]
      </li>
    </ul>

    <h2>th:block</h2>
    <p>th:block 으로 감싸서 th를 사용이 가능하다!</p>
    <p>th:block th:each="dto : ${list}"</p>
    <p>th:text="${dto.sno % 5 == 0 } ? ${dto.sno} : ${dto.first}"</p>
    <p>/th:block</p>
    <ul>
      <th:block th:each="dto : ${list}">
        <li th:text="${dto.sno % 5 == 0 } ? ${dto.sno} : ${dto.first}"></li>
      </th:block>
    </ul>
  </body>
</html>
```

## 3 ) Thymeleaf를 사용해 데이터 출력 [ inline ]

### Controller🔽

```java
@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {
  //요청 URL : localhost:8080/sample/exInline -- Redirect로 --> ex03으로 이동
  /**
       * @Description :  Inline 기능에서 주의 깊게 봐야 하는것은 javascript 부분이다.
       *
       *                 ✔ <script th:inline="javascript">
       *                     let dto = [[${dto}]]
       *                   </script>
       *                   설정만으로 다른 설정없이 JSON 데이터로 받아서
       *                   사용이 가능하다
       *
       *                 🎈 주의사항 : addAttribute() 와 addFlashAttribute() 의 차이점은
       *                             해당 redirect로 페이지가 이동 됐을 시
       *                              - addFlashAttribute() 경우 URL 데이터가 남지 않고 redirect를 받은 화면에서 해당
       *                               전달해준 데이터를 사용 가능 [ 일회성 - URL에 기록이 남지않음 (POST와 흡사함) ]
       *
       *                              - addAttribute()의 경우 데이터 값이 URL에 남으며 해당 값을 Target Contoller에서
       *                                다시 받아줘야한다. [ 다회성 가능 새로고침해도 URL은 남아있기 때문임 (GET 방식) ]
       *                               ❌ 또한 객체 형태를 addAttribute()로 넘기면 Error!
       *                                  Error Msg : Failed to convert value of type 'org.zerock.ex03.DTO.SampleDTO'
       *                                              to required type 'java.lang.String'; nested exception is java.lang.
       *                                              IllegalStateException: Cannot convert value of type 'org.zerock.ex03
       *                                              .DTO.SampleDTO' to required type 'java.lang.String': no matching
       *                                              editors or conversion strategy found
       * */
  @GetMapping({"/exInline"})
  public String exInline(RedirectAttributes redirectAttributes){
      log.info("exInline .... This used RedirectAttributes ");
      SampleDTO dto = SampleDTO.builder().sno(100L).first("First 100").last("Last 100").regTime(LocalDateTime.now()).build();
      redirectAttributes.addFlashAttribute("result","success");
      redirectAttributes.addFlashAttribute("dto",dto);
      // 해당 URL 접근 시 데이터를 포함하여 redirect 시켜버림
      return "redirect:/sample/ex3";
  }
  
  /**
   * @Description :  상위 exInline() 와 비교
   *               - exInline                 : redirectAttributes.addFlashAttribute("Key" , value);
   *               - compareAttrWithFlashAttr : redirectAttributes.addAttribute("Key" , value);
   */
  @GetMapping("/compareAttr")
  public String compareAttrWithFlashAttr(RedirectAttributes redirectAttributes){
  
      log.info("Compare with Attr");
  
      log.info("exInline .... This used RedirectAttributes ");
      SampleDTO dto = SampleDTO.builder().sno(100L).first("First 100").last("Last 100").regTime(LocalDateTime.now()).build();
      redirectAttributes.addAttribute("result","success");
      redirectAttributes.addAttribute("dto",dto);
  
      return "redirect:/sample/ex3";
  }
  
  @GetMapping("/ex3")
  public void ex3(){
      log.info("ex3 ............ I'm GetMapping ");
  }
}
```

### View🔽
```html
<!-- Html [thymeleaf] -->

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    <h1>ex3</h1>

    <h2 th:text="${result}"></h2>
    <h2 th:text="${dto}"></h2>

    <script th:inline="javascript">
      /**
       * 주의깊게 볼 부분 console.log 확인 시
       * - 문자열일 경우는 자동으로 ""로 감싸 문자열로
       * - JSON 형식 데이터일경우 자동으로 JSON.parse() 형태로 JSON으로 포맷해줌
      */
      let msg = [[${result}]];
      let dto = [[${dto}]]
      console.log("msg",msg);
      console.log("dto",dto);
    </script>
  </body>
</html>
```

## 4 ) Thymeleaf의 링크 처리 및 레이아웃

### View (링크)🔽

```html
<!-- html -->

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    <h1>th:Link Test</h1>
    <p>@{} 를 사용한다!</p>

    <h3>Get 방식 URL 만들기</h3>
    <p>th:href="@{/sample/exView(sno=${dto.sno})}"</p>
    <p>result :: /sample/exView?sno=0</p>
    <ul>
      <th:block th:each="dto : ${list}">
        <li>
          <a th:href="@{/sample/exView(sno=${dto.sno})}">[[${dto}]]</a>
        </li>
      </th:block>
    </ul>

    <h3>Get 방식 URL 만들기 __ PathVariable 방식</h3>
    <p>th:href="@{/sample/exView/{sno}(sno = ${dto.sno})}"</p>
    <p>result :: /sample/exView/0 ..... 10</p>
    <ul>
      <th:block th:each="dto : ${list}">
        <li>
          <a th:href="@{/sample/exView/{sno}(sno = ${dto.sno})}">[[${dto}]]</a>
        </li>
      </th:block>
    </ul>

    <h3>Java8Time</h3>
    <p>
      사용하기 위해서는 build.gradle 에 <br />
      implementation group: 'org.thymeleaf.extras', name:
      'thymeleaf-extras-java8time' 를<br />
      추가해줘야함
    </p>
    <p>
      "dto"는기본적으로 앞에서 th:each 로 넘겨준 값임
      [[${#temporals.format(dto.regTime,'yyyy/MM/dd')}]]
    </p>
    <ul>
      <th:block th:each="dto : ${list}">
        <li>
          [[${dto.sno}]] ---------------
          [[${#temporals.format(dto.regTime,'yyyy/MM/dd')}]]
        </li>
      </th:block>
    </ul>
  </body>
</html>
```

### View (레이아웃  - include 처리방식 )🔽

#### Replace, Insert 방식

```Html
<!-- fragment1.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    <div th:fragment = "part1">
        <h2>Part 1 이렇게</h2>
    </div>

    <div th:fragment = "part2">
        <h2>Part 2 나눴</h2>
    </div>

    <div th:fragment = "part3">
        <h2>Part 3 습니다</h2>
    </div>
</body>
</html>


<!-- =============================================================== -->


<!-- fragment2.html -->
<div>
    <hr/>
    <h2>Fragment2 File</h2>
    <h2>Fragment2 File</h2>
    <h2>Fragment2 File</h2>
    <hr/>
</div>



<!-- =============================================================== -->


<!-- exLayout1.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>Fragment Test</h1>

    <p>frgment2.html 파일 자체를 가져옴! </p>
    <p>th:block th:replace="~{/fragments/fragment2}"</p>
    <p>해당 방법은 :: "tagetId" 가 없으므로 전체를 가져온다</p>
    <div style="border: 1px solid red">
        <th:block th:replace="~{/fragments/fragment2}"></th:block>
    </div>


    <p>------------------------------------------------------</p>

    <h2>Layout 1 - 1</h2>
    <p>th:replace="~{/fragments/fragment1::part1}"</p>
    <p>사용시 해당 영역 자체를 덮어씌워버림</p>
    <div th:replace="~{/fragments/fragment1::part1}" ></div>

    <h2>Layout 1 - 2</h2>
    <p> th:insert="~{/fragments/fragment1 :: part2}"</p>
    <p>사용시 해당 영역 안에 해당 타겟을 불러옴 div가 2겹임!!</p>
    <div th:insert="~{/fragments/fragment1 :: part2}"></div>

    <h2>Layout 1 - 3</h2>
    <p>th:block  th:replace="~{/fragments/fragment1 :: part3}"</p>
    <p>사용시 해당 th:block 영역에 div를 가져다 넣음!</p>
    <th:block  th:replace="~{/fragments/fragment1 :: part3}"></th:block>
</body>

</html>

```

#### 파라미터 처리(replace ~ target...) 방식

```html
<!-- html -->

<!-- frgment3.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    <div th:fragment="target(first, second)">
      <style>
        .c1 {
          background-color: red;
        }
        .c2 {
          background-color: blue;
        }
      </style>

      <div class="c1">
        <th:block th:replace="${first}"></th:block>
      </div>
      <div class="c2">
        <th:block th:replace="${second}"></th:block>
      </div>
    </div>
  </body>
</html>

<!-- =============================================================== -->

<!-- exLayout2.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="UTF-8" />
    <title>Title</title>
  </head>
  <body>
    <p>매서드를 이용하는 방식으로 가져오는 방식임</p>
    <p>
      th:block th:replace="~{/fragments/fragment3::target( ~{this::#ulFirst} ,
      ~{this::#ulSecond} ) }"
    </p>
    <th:block
      th:replace="~{/fragments/fragment3::target( ~{this::#ulFirst} , ~{this::#ulSecond} ) }"
    ></th:block>

    <ul id="ulFirst">
      <li>AAA</li>
      <li>BBB</li>
      <li>CCC</li>
    </ul>
    <ul id="ulSecond">
      <li>111</li>
      <li>222</li>
      <li>333</li>
    </ul>
  </body>
</html>
```

- 파라미터 처리(replace ~ target...) 방식2 (조금 더 복잡함)

```html
<!-- html -->

<!-- layout1.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<!-- setContent(content)메서드를  만들며 content라는 파라미터를 받을것임-->
<th:block th:fragment="setContent(content)">

<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<style>
    * {
        margin: 0;
        padding: 0;
    }
    .header {
        width:100vw;
        height: 20vh;
        background-color: aqua;
    }
    .content {
        width: 100vw;
        height: 70vh;
        background-color: lightgray;
    }
    .footer {
        width: 100vw;
        height: 10vh;
        background-color: green;
    }
</style>


    <div class="header">
        <h1>HEADER</h1>
    </div>
    <div class="content" >
        <th:block th:replace = "${content}">
        </th:block>
    </div>

    <div class="footer">
        <h1>FOOTER</h1>
    </div>

</body>
</th:block>
</html>


<!-- =============================================================== -->


<!-- exTemplate.html -->
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
  <!-- 내용 전체를 layout1.html로 덮어씌움 -->
  <th:block th:replace="~{/layout/layout1 :: setContent(~{this::content} )}">
    <!-- 단 덮어씌우면서 content는 넘겨서 lauout1.html에서 처리하게함 -->
    <th:block th:fragment="content">
      <h1>exTemplate Page</h1>
    </th:block>
  </th:block>
</html>


```
