package com.back.cinetalk.rate.dto;

import com.back.cinetalk.rate.entity.RateEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(description = "리뷰 데이터 담기는 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO {

    private Long id;

    private Long reviewId;

    private Long rereviewId;

    private Long userId;

    private int rate;

    public static RateDTO ToRateDTO(RateEntity rateEntity){
        return RateDTO.builder()
                .id(rateEntity.getId())
                .reviewId(rateEntity.getReviewId())
                .rereviewId(rateEntity.getRereviewId())
                .userId(rateEntity.getUserId())
                .rate(rateEntity.getRate())
                .build();
    }
}
