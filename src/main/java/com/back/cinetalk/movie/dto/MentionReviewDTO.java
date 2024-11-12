package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MentionReviewDTO {

    private Long id;
    private Long movieId;
    private String movienm;
    private double star;
    private String content;
    private int createdAt;
    private byte[] profile;
    private String nickname;
}
