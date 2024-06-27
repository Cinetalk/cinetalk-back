package com.back.cinetalk.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPreViewDTO {

    String nickName;

    Double star;

    String content;

    LocalDateTime createdAt;

    boolean spoiler;

    long likeCount;

    long dislikeCount;
}
