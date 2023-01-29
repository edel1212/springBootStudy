package com.yoo.ex04.mockTestPackage;

import com.yoo.ex04.cotroller.ReplyController;
import com.yoo.ex04.repository.ReplyRepository;
import com.yoo.ex04.service.ReplyService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class) // JUnit 과 Mockito를 연동
@Log4j2
public class MockTestClass {

    @InjectMocks
    private ReplyController replyController;

    @Mock
    private ReplyRepository replyRepository;

    private MockMvc mockMvc; //perform을 이용해서 URL 테스트가 가능함

    @BeforeEach
    public void inti(){
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
    }


    @Test
    public void testMethod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/replies/board/90")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andDo(log::info);
    }




}
