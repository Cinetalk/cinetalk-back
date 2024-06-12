package com.back.cinetalk.review.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rereview.entity.ReReviewEntity;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review_genre.ReviewGenreEntity;
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

    @OneToMany(mappedBy = "review")
    private List<ReReviewEntity> reReviewEntityList = new ArrayList<ReReviewEntity>();

    @OneToMany(mappedBy = "review")
    private List<ReviewGenreEntity> reviewGenreEntityList = new ArrayList<ReviewGenreEntity>();

    public void update(ReviewRequestDTO reviewRequestDTO) {
        this.star = reviewRequestDTO.getStar();
        this.content = reviewRequestDTO.getContent();
        this.spoiler = reviewRequestDTO.isSpoiler();
    }

}
