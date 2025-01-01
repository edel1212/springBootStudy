package com.yoo.springBootMailStudy.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
            String htmlContent = "<h1>메일입니다!!</h1>"; // 이미지를 참조하는 HTML 태그

            helper.setText(htmlContent, true); // true를 설정하여 HTML 메일로 인식하게 함

            // 이미지 파일을 첨부
            ClassPathResource image = new ClassPathResource("static/img/test.jpg");
            helper.addInline("logo", image);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
