package com.back.cinetalk.review.dto;


import com.back.cinetalk.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StarResponseDTO {
    private Long reviewId;
    private double star;

    public static StarResponseDTO toStarResponseDTO(ReviewEntity reviewEntity) {
        return StarResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .star(reviewEntity.getStar())
                .build();
    }
}
