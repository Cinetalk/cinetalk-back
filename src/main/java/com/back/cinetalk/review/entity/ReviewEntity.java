package com.back.cinetalk.review.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rate.dislike.entity.ReviewDislikeEntity;
import com.back.cinetalk.rate.like.entity.ReviewLikeEntity;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.review.dto.CommentRequestDTO;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Long movieId;

    private String movienm;

    private Double star;

    private String content;

    private boolean spoiler;

    private boolean isEdited;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ReviewEntity parentReview; //부모 리뷰

    @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL)
    private List<ReviewEntity> childrenComment = new ArrayList<>(); //자식 댓글들(리뷰에 대한 댓글)

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewGenreEntity> reviewGenreEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewLikeEntity> reviewLikeEntityList = new ArrayList<>(); // 좋아요 리스트

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewDislikeEntity> reviewDislikeEntityList = new ArrayList<>(); // 싫어요 리스트

    @OneToMany(mappedBy = "review",cascade = CascadeType.ALL)
    private List<ReportEntity> reportEntityList = new ArrayList<>(); // 신고 리스트

    public void updateReview(ReviewRequestDTO reviewRequestDTO) {
        this.star = reviewRequestDTO.getStar();
        this.content = reviewRequestDTO.getContent();
        this.spoiler = reviewRequestDTO.isSpoiler();
        this.isEdited = true;
    }

    public void updateComment(CommentRequestDTO commentRequestDTO) {
        this.content = commentRequestDTO.getContent();
        this.isEdited = true;
    }

    public void updateReviewContent(String content) {
        this.content = content;
    }
}
