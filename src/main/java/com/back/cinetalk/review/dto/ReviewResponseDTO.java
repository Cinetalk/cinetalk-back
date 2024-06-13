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
public class ReviewResponseDTO {

    Long reviewId;

    LocalDateTime createdAt;

    public static ReviewResponseDTO toReviewResponseDTO(ReviewEntity reviewEntity){
        return ReviewResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
