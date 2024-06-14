package com.back.cinetalk.review.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
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

    @ManyToOne(fetch = FetchType.EAGER)
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

    public void update(ReviewRequestDTO reviewRequestDTO) {
        this.star = reviewRequestDTO.getStar();
        this.content = reviewRequestDTO.getContent();
        this.spoiler = reviewRequestDTO.isSpoiler();
    }

}
