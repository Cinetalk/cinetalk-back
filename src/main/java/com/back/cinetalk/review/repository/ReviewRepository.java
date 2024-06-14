package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> , QuerydslPredicateExecutor<ReviewEntity>, ReviewRepositoryCustom {

   boolean existsByUserIdAndMovieId(Long userId, Long movieId);

   List<ReviewEntity> findTop10ByContentContainingAndParentReviewIsNullOrderByCreatedAtAsc(String Content);
}