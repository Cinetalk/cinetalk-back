package com.back.cinetalk.report.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "Report")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String content;

    //신고가 처리된건지 판단하기 위한 status 컬럼 추가
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;
}
