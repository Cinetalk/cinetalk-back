package com.back.cinetalk.report.service;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.repository.KeywordRepository;
import com.back.cinetalk.report.dto.KeywordReportRequestDTO;
import com.back.cinetalk.report.dto.ReviewReportRequestDTO;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.report.repository.ReportRepository;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    @Transactional
    public StateRes saveReviewReport(Long reviewId, ReviewReportRequestDTO reviewReportRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));

        if (reportRepository.existsByUserAndReview(user, review)) {
            throw new RestApiException(CommonErrorCode.REVIEW_REPORT_ALREADY_IN_WRITE);
        }

        reportRepository.save(ReportEntity.builder()
                .category(reviewReportRequestDTO.getCategory())
                .content(reviewReportRequestDTO.getContent())
                .user(user)
                .review(review)
                .build()
        );

        return new StateRes(true);
    }

    @Transactional
    public StateRes saveKeywordReport(Long keywordId, KeywordReportRequestDTO keywordReportRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);
        KeywordEntity keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.KEYWORD_NOT_FOUND));

        if (reportRepository.existsByUserAndKeyword(user, keyword)) {
            throw new RestApiException(CommonErrorCode.KEYWORD_REPORT_ALREADY_IN_WRITE);
        }

        reportRepository.save(
                ReportEntity.builder()
                        .movieId(keyword.getMovieId())
                        .content(keywordReportRequestDTO.getContent())
                        .user(user)
                        .build());

        return new StateRes(true);
    }
}
