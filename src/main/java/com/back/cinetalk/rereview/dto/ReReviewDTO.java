package com.back.cinetalk.rereview.dto;

import com.back.cinetalk.rereview.entity.ReReviewEntity;
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
public class ReReviewDTO {

    private Long id;

    private int review_id;

    private int user_id;

    private String content;

    private LocalDateTime regdate;

    public static ReReviewDTO ToReReviewDTO(ReReviewEntity rereviewEntity){
        return ReReviewDTO.builder()
                .id(rereviewEntity.getId())
                .review_id(rereviewEntity.getReview_id())
                .user_id(rereviewEntity.getUser_id())
                .content(rereviewEntity.getContent())
                .regdate(rereviewEntity.getRegdate())
                .build();
    }
}
