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
public class ReReviewResponseDTO {

    Long reReviewId;

    LocalDateTime createdAt;

    public static ReReviewResponseDTO toReReviewResponseDTO(ReviewEntity reviewEntity) {
        return ReReviewResponseDTO.builder()
                .reReviewId(reviewEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
