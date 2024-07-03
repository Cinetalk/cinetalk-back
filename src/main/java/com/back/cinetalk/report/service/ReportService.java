package com.back.cinetalk.report.service;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.report.dto.ReportRequestDTO;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.report.repository.ReportRepository;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public StateRes saveReport(Long reviewId, ReportRequestDTO reportRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));

        if (reportRepository.existsByUserAndReview(user, review)) {
            throw new RestApiException(CommonErrorCode.REPORT_ALREADY_IN_WRITE);
        }

        reportRepository.save(
                ReportEntity.builder()
                        .category(reportRequestDTO.getCategory())
                        .content(reportRequestDTO.getContent())
                        .user(user)
                        .review(review)
                        .build());

        return new StateRes(true);
    }
}
