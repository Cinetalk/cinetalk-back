package com.back.cinetalk.review.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.rate.entity.RateEntity;
import com.back.cinetalk.review.dto.CommentRequestDTO;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
// 한개의 영화에서는 한개의 리뷰만 작성
@Table(name = "Review", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_id"})
})
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ReviewEntity parentReview; //부모 리뷰

    @OneToMany(mappedBy = "parentReview", orphanRemoval = true)
    private List<ReviewEntity> childrenComment = new ArrayList<ReviewEntity>(); //자식 댓글들(리뷰에 대한 댓글)

    @OneToMany(mappedBy = "review")
    private List<RateEntity> rateEntityList = new ArrayList<RateEntity>();

    public void updateReview(ReviewRequestDTO reviewRequestDTO) {
        this.star = reviewRequestDTO.getStar();
        this.content = reviewRequestDTO.getContent();
        this.spoiler = reviewRequestDTO.isSpoiler();
    }

    public void updateComment(CommentRequestDTO commentRequestDTO) {
        this.content = commentRequestDTO.getContent();
    }


}
