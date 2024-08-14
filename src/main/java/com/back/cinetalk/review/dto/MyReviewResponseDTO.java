package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewResponseDTO {

    private Long reviewId;

    private Double star;

    private String content;

    private LocalDateTime createTime;

    public static MyReviewResponseDTO toMyReviewResponseDTO(ReviewEntity reviewEntity) {
        return MyReviewResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .createTime(reviewEntity.getCreatedAt())
                .build();
    }
}
