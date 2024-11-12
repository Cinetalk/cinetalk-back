package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopTenReviewDTO {

    private Long reviewId;

    private Double star;

    private String content;

    private Long likeCount;

    //private byte[] profile;


}
