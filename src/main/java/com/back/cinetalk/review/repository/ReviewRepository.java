package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

    @Repository
    public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> , QuerydslPredicateExecutor<ReviewEntity> {

    }
