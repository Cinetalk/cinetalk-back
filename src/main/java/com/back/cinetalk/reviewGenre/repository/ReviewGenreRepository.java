package com.back.cinetalk.reviewGenre.repository;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewGenreRepository extends JpaRepository<ReviewGenreEntity, Long> {

    List<ReviewGenreEntity> findByReview(ReviewEntity review);
}
