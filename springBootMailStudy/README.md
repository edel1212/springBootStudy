# SMTP 테스트

- gmail을 기반으로 테스트
- 스큐리티 적용 X
- 테스트용이기에 Interface 구조를 사용하지 않고 구현
- 서버 로직 구현전 Google에서 앱 비밀번호 설정이 필요함
  - google → 계정관리 → 보안 → 2단계 인증 → 앱 비밀번호 설정
- 순수 메일 발송 테스트
  - 일반 메세지만 보내기
  - HTML코드 형식으로 보내기
  - 파일을 첨부하여 보니rl

### 사용 방법
- 의존성 추가
  - `implementation 'org.springframework.boot:spring-boot-starter-mail'`
- SMTP 대상 서버 사용 설정
    ```properties
    # Mail Setting
  mail:
    host: smtp.gmail.com # 1
    port: 587 # 2
    username: {계정정보} # 3
    password: {패스워드} # 4
    properties:
      mail:
        smtp:
          auth: true # 5
          timeout: 5000 # 6
          starttls:
            enable: true # 7
    ```
- 비즈니스 로직
  ```java
  @Service
  @Log4j2
  @RequiredArgsConstructor
  public class EmailService {
  private final JavaMailSender javaMailSender;
  
      public void mailSend() {
          SimpleMailMessage msg = new SimpleMailMessage();
          msg.setTo("edel1212@naver.com");
          msg.setSubject("흑곰님입니다.");  // 제목
          msg.setText("!!!! 내용입니다."); //  내용
          javaMailSender.send(msg);
      }
  
      public void mailSendToHTML() {
          try {
              MimeMessage mimeMessage = javaMailSender.createMimeMessage();
              MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
              helper.setTo("edel1212@naver.com");
              helper.setSubject("와구와구과구!!!");
  
              // HTML 콘텐츠를 문자열로 작성
              String htmlContent = "<h1>안녕하세요, 흑곰님입니다!</h1>" +
                      "<p>!!!! 내용입니다.</p>" +
                      "<p>HTML 형식의 <strong>이메일</strong>을 보내드립니다.</p>";
  
              helper.setText(htmlContent, true); // true를 설정하여 HTML 메일로 인식하게 함
  
              javaMailSender.send(mimeMessage);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
      public void mailSendToHTMLAddFile(){
          try {
              MimeMessage mimeMessage = javaMailSender.createMimeMessage();
              MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // multipart 모드 활성화
  
              helper.setTo("edel1212@naver.com");
              helper.setSubject("흑곰님입니다.");
  
              // HTML 콘텐츠를 문자열로 작성
              String htmlContent = "<h1>안녕하세요, 흑곰님입니다!</h1>" +
                      "<p>!!!! 내용입니다.</p>" +
                      "<p>HTML 형식의 <strong>이메일</strong>을 보내드립니다.</p>"; // 이미지를 참조하는 HTML 태그
  
              helper.setText(htmlContent, true); // true를 설정하여 HTML 메일로 인식하게 함
  
              // 이미지 파일을 첨부
              ClassPathResource image = new ClassPathResource("static/img/test.jpg");
              helper.addInline("test.png", image);
  
              javaMailSender.send(mimeMessage);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
  
  }
    ```
  

---

## HTML 임배디드 내 이미지 추가
- 방법
  - InLine 방식을 통한 파일 첨부 : 해당 방법을 지원해 주지 않는 메일 서비스가 있음 [비추천]
  - CID를 통한 메일 첨부
  - 외부 접근이 가능한 서버를 사용하여 이미지를 불러오는 방식
- 테스트는 로컬 환경에서 진행하므로 CID 방식을 설명함
  - 주의 사항
    - HTML 요소를 만든 후 파일을 주입해야 정상적으로 이미지가 cid경로애 들어가 있다!! 
      - 그렇지 않을 경우 attach(0).txt 파일 형태로 파일이 첨부되어 있고 이미지는 엑박 상태임
    - `addInLine()`에 넣어주는 키 값을 꼭 `cid`명칭 과 맞추자'
      - `"<img src='cid:logo'>"`  ----> `helper.addInline("logo", image);`
- 예시 코드
  ```java
  class MailTest{
    public void mailSendToHTMLAddCid(){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // multipart 모드 활성화

            helper.setTo("edel1212@naver.com");
            helper.setSubject("흑곰님입니다.");

            // HTML 콘텐츠를 문자열로 작성
            String htmlContent = "<img src='cid:logo'>"; // 이미지를 참조하는 HTML 태그

            helper.setText(htmlContent, true); // true를 설정하여 HTML 메일로 인식하게 함


            /**************************/
            /***  🔥 초 삽질했었음 ...  */
            // setText()를 통해 먼저 선행으로 Dom요소를 만든 후 cid를 추가
            // 파일 추가 순서로 가야지 진행됬었음
            // 이미지 파일 첨부를 그 윗 단계  String htmlContent = "<img src='cid:logo'>"; 이전 에서
            // 진행 하여 이미지 첨부가 안되고 첨부파일 내 attachg(0).txt 만들어가는 이슈가 있었다 ..
            /**************************/

            // 이미지 파일을 첨부  !!!! ⭐ 순서 초 중요!!!
            ClassPathResource image = new ClassPathResource("static/img/test.jpg");
            helper.addInline("logo", image);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }
  ```

## 메일 발송 시 발신자명 추가 방법
- 별거 없다 `MimeMessageHelper` 객체 내 함수애서 setFrom을 통해 설정해주면 된다.
  - Ex) `setFrom(발신자메일 , 발신자명)`
- 예시 코드
  ```java
  class MailTest{
     public void mailSendChangeFromName(){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // 보낸 사람의 이름과 주소 설정
            helper.setFrom("sender@example.com", "보낸 사람 이름");

            // 받는 사람
            helper.setTo("edel1212@naver.com");
            helper.setSubject("메일 제목입니다.");

            String htmlContent = "<h1>발송자명 변경 테스트</h1>";

            helper.setText(htmlContent, true); // true를 설정하여 HTML 메일로 인식하게 함

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  }
  ```
  
