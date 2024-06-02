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

    private int review_id;

    private int rereview_id;

    private int user_id;

    private int rate;

    public static RateDTO ToRateDTO(RateEntity rateEntity){
        return RateDTO.builder()
                .id(rateEntity.getId())
                .review_id(rateEntity.getReview_id())
                .rereview_id(rateEntity.getRereview_id())
                .user_id(rateEntity.getUser_id())
                .rate(rateEntity.getRate())
                .build();
    }
}
