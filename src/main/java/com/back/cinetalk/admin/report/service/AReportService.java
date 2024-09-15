package com.back.cinetalk.admin.report.service;

import com.back.cinetalk.admin.report.dto.AReportListDTO;
import com.back.cinetalk.report.entity.QReportEntity;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AReportService {

    private final JPAQueryFactory queryFactory;
    QReportEntity report = QReportEntity.reportEntity;

    @Transactional(readOnly = true)
    public ResponseEntity<?> aReportList(){

        List<AReportListDTO> resultList = queryFactory.select(Projections.constructor(
                AReportListDTO.class,
                report.id,
                report.category,
                report.content,
                report.status,
                report.review.content.as("review_content"),
                report.user.email.as("user_email"),
                report.user.nickname.as("user_nickName"),
                report.createdAt
                ))
                .from(report)
                .where(report.status.eq(false))
                .orderBy(report.createdAt.desc())
                .fetch();

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
