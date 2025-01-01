package com.yoo.springBootMailStudy.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("이메일 전송")
    void simpleMailSender() {
        emailService.mailSend();
    }

    @Test
    void mailToHTML() {
        emailService.mailSendToHTML();
    }

    @Test
    @DisplayName("HTML 본문 및 이미지 첨부")
    void mailToHTMLAddFile() {
        emailService.mailSendToHTMLAddFile();
    }

    @Test
    @DisplayName("HTML Cid 이미지 첨부")
    void mailToHTMLAddCid() {
        emailService.mailSendToHTMLAddCid();
    }

    @Test
    @DisplayName("이메일 발송 - 발신자명 변경")
    void mailSendChangeFromName() {
        emailService.mailSendChangeFromName();
    }
}