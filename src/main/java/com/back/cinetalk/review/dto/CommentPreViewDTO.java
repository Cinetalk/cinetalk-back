package com.back.cinetalk.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPreViewDTO {
    Long id;

    String nickName;

    byte[] profileImage;

    String content;

    LocalDateTime createdAt;

    long likeCount;

    long dislikeCount;

    boolean likeCheck;

    boolean dislikeCheck;

    boolean isMine;

    boolean isEdited;
}
