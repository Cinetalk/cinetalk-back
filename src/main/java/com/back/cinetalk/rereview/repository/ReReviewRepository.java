package com.back.cinetalk.rereview.repository;

import com.back.cinetalk.rereview.entity.ReReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReReviewRepository extends JpaRepository<ReReviewEntity, Long> , QuerydslPredicateExecutor<ReReviewEntity> {

}
