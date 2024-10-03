package com.back.cinetalk.admin.damage.service;

import com.back.cinetalk.admin.damage.dto.DamageRequestDTO;
import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.damage.entity.DamageEntity;
import com.back.cinetalk.damage.repository.DamageRepository;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.report.repository.ReportRepository;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ADamageService {

    private final ReportRepository reportRepository;
    private final DamageRepository damageRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public StateRes ADamage(Long report_id, DamageRequestDTO requestDTO){

        ReportEntity report = reportRepository.findById(report_id).orElse(null);

        if(report==null){
            throw new RestApiException(CommonErrorCode.REPORT_NOT_FOUND);
        }

        LocalDate today = LocalDate.now();

        DamageEntity damage = DamageEntity.builder()
                .category(requestDTO.getCategory())
                .movienm(report.getReview().getMovienm())
                .content(report.getReview().getContent())
                .user(report.getReview().getUser())
                .startDate(today)
                .endDate(today.plusDays(requestDTO.getDate()))
                .build();

        Long id = report.getReview().getId();

        //제재 테이블 저장
        damageRepository.save(damage);

        //신고에는 신고완료로 체크
        report.UpdateStatus(true);

        return new StateRes(true);
    }

    public StateRes ADamageReview(Long report_id){

        ReportEntity report = reportRepository.findById(report_id).orElse(null);

        ReviewEntity review = report.getReview();

        review.updateReviewContent("<신고된 리뷰 입니다.>");

        reviewRepository.save(review);

        return new StateRes(true);
    }
}
