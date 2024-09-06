package com.back.cinetalk.admin.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackListDTO {

    private Long id;

    private String user_email;

    private String user_nickName;

    private String content;
}
