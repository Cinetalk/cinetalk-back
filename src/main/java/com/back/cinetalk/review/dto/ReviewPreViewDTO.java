package com.back.cinetalk.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPreViewDTO {

    Long id;

    String nickName;

    byte[] profileImage;

    List<String> badgeList;

    Double star;

    String content;

    LocalDateTime createdAt;

    boolean spoiler;

    long likeCount;

    long dislikeCount;

    long commentCount;

    boolean likeCheck;

    boolean dislikeCheck;

    boolean isMine;

    @QueryProjection
    public ReviewPreViewDTO(Long id, String nickName, byte[] profileImage, Double star, String content, LocalDateTime createdAt, boolean spoiler, long likeCount, long dislikeCount, long commentCount, boolean likeCheck, boolean dislikeCheck, boolean isMine) {
        this.id = id;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.star = star;
        this.content = content;
        this.createdAt = createdAt;
        this.spoiler = spoiler;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.likeCheck = likeCheck;
        this.dislikeCheck = dislikeCheck;
        this.isMine = isMine;
    }
}
