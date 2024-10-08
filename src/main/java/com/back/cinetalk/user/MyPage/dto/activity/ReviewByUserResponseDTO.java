package com.back.cinetalk.user.MyPage.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewByUserResponseDTO {

    private Long review_id;
    private Long movie_id;
    private String movienm;
    private String poster_id;
    private Double star;
    private String content;
    private Long RateCount;
    private int RereviewCount;
    private String regDate;
}
