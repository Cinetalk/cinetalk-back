package com.back.cinetalk.review.repository;

import com.back.cinetalk.rate.entity.RateEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> , QuerydslPredicateExecutor<ReviewEntity> {

    List<ReviewEntity> findReviewsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    Page<ReviewEntity> findAllByMovieId(Long movieId, Pageable pageable);
}
