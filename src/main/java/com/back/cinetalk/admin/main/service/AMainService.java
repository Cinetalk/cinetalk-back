package com.back.cinetalk.admin.main.service;

import com.back.cinetalk.admin.main.dto.MainResponseDTO;
import com.back.cinetalk.keyword.entity.QKeywordEntity;
import com.back.cinetalk.review.dto.ReviewResponseDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AMainService {

    private final JPAQueryFactory queryFactory;
    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QKeywordEntity keyword = QKeywordEntity.keywordEntity;

    LocalDateTime sixAgo = LocalDate.now()
            .minusMonths(5)
            .withDayOfMonth(1)
            .atStartOfDay();

    public ResponseEntity<?> userCountList(){

        StringTemplate yearMonth = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", user.createdAt);

        List<MainResponseDTO> resultList = queryFactory.select(Projections.constructor(
                    MainResponseDTO.class,
                    yearMonth,
                    user.count()))
                .from(user)
                .where(user.createdAt.after(sixAgo))
                .groupBy(yearMonth)
                .fetch();

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<?> reviewCountList(){

        StringTemplate yearMonth = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", review.createdAt);

        List<MainResponseDTO> resultList = queryFactory.select(Projections.constructor(
                    MainResponseDTO.class,
                    yearMonth,
                    review.count()))
                .from(review)
                .where(review.createdAt.after(sixAgo)
                        .and(review.parentReview.isNull()))
                .groupBy(yearMonth)
                .fetch();

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<?> keywordList(){

        StringTemplate yearMonth = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", keyword.createdAt);

        List<MainResponseDTO> resultList = queryFactory.select(Projections.constructor(
                    MainResponseDTO.class,
                    yearMonth,
                    keyword.count()))
                .from(keyword)
                .where(keyword.createdAt.after(sixAgo))
                .groupBy(yearMonth)
                .fetch();

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
