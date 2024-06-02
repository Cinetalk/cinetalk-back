package com.back.cinetalk.rate.repository;

import com.back.cinetalk.rate.entity.RateEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<RateEntity, Long> , QuerydslPredicateExecutor<RateEntity> {

}
