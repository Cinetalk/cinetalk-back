package com.back.cinetalk.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPreViewDTO {

    String nickName;

    String content;

    LocalDateTime createdAt;
}
