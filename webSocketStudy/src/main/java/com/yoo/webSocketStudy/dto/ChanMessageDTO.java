package com.yoo.webSocketStudy.dto;

import com.yoo.webSocketStudy.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChanMessageDTO {
    private MessageType messageType;
    private String content;
    private String sender;
}
