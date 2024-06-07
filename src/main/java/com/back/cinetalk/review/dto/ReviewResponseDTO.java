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
public class ReviewResponseDTO {

    private Long id;

    private Long movieId;

    private Long userId;

    private Double star;

    private String content;

    public static ReviewResponseDTO toReviewResponseDTO(ReviewEntity reviewEntity){
        return ReviewResponseDTO.builder()
                .id(reviewEntity.getId())
                .movieId(reviewEntity.getMovieId())
                .userId(reviewEntity.getUserId())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .build();
    }
}
