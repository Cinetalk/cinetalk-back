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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AReportService {

    private final JPAQueryFactory queryFactory;

    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QReportEntity report = QReportEntity.reportEntity;

    public ResponseEntity<?> aReportList(){

        List<AReportListDTO> resultList = queryFactory.select(Projections.constructor(
                AReportListDTO.class,
                report.id,
                report.category,
                report.content,
                report.status,
                report.review.content.as("review_content"),
                report.user.email.as("user_email"),
                report.user.nickname,
                report.createdAt
                ))
                .from(report)
                .where(report.status.eq(false))
                .orderBy(report.createdAt.desc())
                .fetch();

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<?> aReportDetail(Long id){

        AReportListDTO result = queryFactory.select(Projections.constructor(
                        AReportListDTO.class,
                        report.id,
                        report.category,
                        report.content,
                        report.status,
                        report.review.content.as("review_content"),
                        report.user.email.as("user_email"),
                        report.user.nickname,
                        report.createdAt
                ))
                .from(report)
                .where(report.status.eq(false)
                .and(report.id.eq(id)))
                .fetchOne();


        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
