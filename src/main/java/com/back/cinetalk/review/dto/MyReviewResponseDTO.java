package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewResponseDTO {

    private Long reviewId;

    private Double star;

    private String content;

    boolean spoiler;

    private LocalDateTime createTime;

    String nickName;

    List<String> badgeList;

    public static MyReviewResponseDTO toMyReviewResponseDTO(ReviewEntity reviewEntity) {
        return MyReviewResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .spoiler(reviewEntity.isSpoiler())
                .createTime(reviewEntity.getCreatedAt())
                .nickName(reviewEntity.getUser().getNickname())
                .badgeList(reviewEntity.getUser().getUserBadgeEntityList()
                        .stream()
                        .map(badge -> badge.getBadge().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
