package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    private Long movieId;

    private String movienm;

    private UserDTO user;

    private Double star;

    private String content;

    private boolean spoiler;

    private ReviewEntity parentReview;

    private String createdAt;

    public static ReviewDTO toReviewDTO(ReviewEntity reviewEntity){

        return ReviewDTO.builder()
                .id(reviewEntity.getId())
                .movieId(reviewEntity.getMovieId())
                .movienm(reviewEntity.getMovienm())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .spoiler(reviewEntity.isSpoiler())
                .createdAt(String.valueOf(reviewEntity.getCreatedAt()).substring(0,4))
                .build();
    }
}
