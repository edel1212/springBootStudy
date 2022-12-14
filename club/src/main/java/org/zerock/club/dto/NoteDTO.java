package org.zerock.club.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.json.GsonJsonParser;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class NoteDTO {

    private Long num;

    private String title;

    private String content;

    private String writerEmail;

    private LocalDateTime regDate, modDate;


    public static void main(String[] args) throws JsonProcessingException {
        NoteDTO  note = NoteDTO.builder()
                .title("Test")
                .content("Test  Content")
                .writerEmail("user90@naver.com")
                .build();
        ObjectMapper mapper = new ObjectMapper();
       log.info(mapper.writeValueAsString(note));
    }
}
