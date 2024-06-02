package com.back.cinetalk.rereview.entity;

import com.back.cinetalk.rereview.dto.ReReviewDTO;
import com.back.cinetalk.review.dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ReReview")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int review_id;

    private int user_id;

    private String content;

    private LocalDateTime regdate;

    @PrePersist
    public void prePersist() {
        this.regdate = LocalDateTime.now(); // 현재 날짜를 설정
    }

    public static ReReviewEntity ToReReviewEntity(ReReviewDTO rereviewDTO){
        return ReReviewEntity.builder()
                .id(rereviewDTO.getId())
                .review_id(rereviewDTO.getReview_id())
                .user_id(rereviewDTO.getUser_id())
                .content(rereviewDTO.getContent())
                .regdate(rereviewDTO.getRegdate())
                .build();
    }
}
