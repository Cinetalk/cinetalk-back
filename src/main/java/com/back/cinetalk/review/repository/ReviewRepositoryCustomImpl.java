package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.ReReviewPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QUserEntity userEntity = QUserEntity.userEntity;

        List<ReviewPreViewDTO> results = queryFactory
                .select(Projections.constructor(
                        ReviewPreViewDTO.class,
                        reviewEntity.user.nickname,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<ReReviewPreViewDTO> findAllByParentReviewId(Long parentReviewId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QUserEntity userEntity = QUserEntity.userEntity;

        List<ReReviewPreViewDTO> results = queryFactory
                .select(Projections.constructor(
                        ReReviewPreViewDTO.class,
                        reviewEntity.user.nickname,
                        reviewEntity.content,
                        reviewEntity.createdAt))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .where(reviewEntity.parentReview.id.eq(parentReviewId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(reviewEntity.parentReview.id.eq(parentReviewId))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(results, pageable, total);
    }
}