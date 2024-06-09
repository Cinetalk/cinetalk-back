package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ReviewPreViewDTO {

    Long userId;

    Double star;

    String content;

    LocalDate createdAt;

    boolean spoiler;

    public static ReviewPreViewDTO toReviewPreViewDTO(ReviewEntity reviewEntity) {
        return ReviewPreViewDTO.builder()
                .userId(reviewEntity.getUserId())
                .star(reviewEntity.getStar())
                .createdAt(reviewEntity.getCreatedAt().toLocalDate())
                .content(reviewEntity.getContent())
                .build();
    }
}
