package com.back.cinetalk.review.entity;

import com.back.cinetalk.review.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int movie_id;

    private String movie_name;

    private int user_id;

    private Double star;

    private String content;

    private LocalDateTime regdate;

    @PrePersist
    public void prePersist() {
        this.regdate = LocalDateTime.now(); // 현재 날짜를 설정
    }

    public static ReviewEntity ToReviewEntity(ReviewDTO reviewDTO){
        return ReviewEntity.builder()
                .id(reviewDTO.getId())
                .movie_id(reviewDTO.getMovie_id())
                .movie_name(reviewDTO.getMovie_name())
                .user_id(reviewDTO.getUser_id())
                .star(reviewDTO.getStar())
                .content(reviewDTO.getContent())
                .regdate(reviewDTO.getRegdate())
                .build();
    }
}
