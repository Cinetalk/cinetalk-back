package com.back.cinetalk.rate.like.repository;

import com.back.cinetalk.rate.like.entity.ReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLikeEntity, Long> {

    long countByReviewId(Long reviewId);

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}
