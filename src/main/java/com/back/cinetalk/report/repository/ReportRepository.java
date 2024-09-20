package com.back.cinetalk.report.repository;

import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    boolean existsByUserAndReview (UserEntity user, ReviewEntity review);

    Optional<ReportEntity> findById(long id);

}
