package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    private Long movieId;

    private UserEntity user;

    private Double star;

    private String content;

    private boolean spoiler;

    public static ReviewDTO toReviewDTO(ReviewEntity reviewEntity){
        return ReviewDTO.builder()
                .id(reviewEntity.getId())
                .movieId(reviewEntity.getMovieId())
                .user(reviewEntity.getUser())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .spoiler(reviewEntity.isSpoiler())
                .build();
    }
}
