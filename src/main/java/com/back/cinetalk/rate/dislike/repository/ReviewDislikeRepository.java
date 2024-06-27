package com.back.cinetalk.rate.dislike.repository;

import com.back.cinetalk.rate.dislike.entity.ReviewDislikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDislikeRepository extends JpaRepository<ReviewDislikeEntity, Long> {

    long countByReviewId(Long reviewId);

    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}
