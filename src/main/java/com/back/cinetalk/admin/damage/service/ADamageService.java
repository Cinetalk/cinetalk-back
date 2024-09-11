package com.back.cinetalk.admin.damage.service;

import com.back.cinetalk.damage.entity.DamageEntity;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.report.dto.ReportRequestDTO;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ADamageService {

    private final ReportRepository reportRepository;

    @Transactional
    public ResponseEntity<?> ADamage(Long report_id, ReportRequestDTO requestDTO){

        ReportEntity report = reportRepository.findById(report_id).orElse(null);

        if(report==null){
            throw new RestApiException(CommonErrorCode.REPORT_NOT_FOUND);
        }

        DamageEntity damage = DamageEntity.builder()
                .category(requestDTO.getCategory())
                .content(report.getReview().getContent())
                .user(report.getReview().getUser())
                .build();



        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
