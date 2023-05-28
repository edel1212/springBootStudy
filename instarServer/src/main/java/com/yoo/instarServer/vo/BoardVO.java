package com.yoo.instarServer.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardVO {
    private String name;
    private String userImage;
    private String postImage;
    private int likes;
    private Date date;
    private boolean liked;
    private String content;
    private String filter;
}
