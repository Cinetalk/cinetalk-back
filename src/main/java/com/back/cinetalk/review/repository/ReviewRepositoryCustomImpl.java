package com.back.cinetalk.review.repository;

import com.back.cinetalk.rate.dislike.entity.QReviewDislikeEntity;
import com.back.cinetalk.rate.like.entity.QReviewLikeEntity;
import com.back.cinetalk.review.dto.CommentPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
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
        QReviewLikeEntity reviewLikeEntity = QReviewLikeEntity.reviewLikeEntity;
        QReviewDislikeEntity reviewDislikeEntity = QReviewDislikeEntity.reviewDislikeEntity;

        List<ReviewPreViewDTO> results = queryFactory
                .select(Projections.constructor(
                        ReviewPreViewDTO.class,
                        reviewEntity.user.nickname,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct()))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .leftJoin(reviewLikeEntity).on(reviewLikeEntity.review.eq(reviewEntity))
                .leftJoin(reviewDislikeEntity).on(reviewDislikeEntity.review.eq(reviewEntity))
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .groupBy(reviewEntity.id)
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
    public Page<CommentPreViewDTO> findAllByParentReviewId(Long parentReviewId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QUserEntity userEntity = QUserEntity.userEntity;
        QReviewLikeEntity reviewLikeEntity = QReviewLikeEntity.reviewLikeEntity;
        QReviewDislikeEntity reviewDislikeEntity = QReviewDislikeEntity.reviewDislikeEntity;

        List<CommentPreViewDTO> results = queryFactory
                .select(Projections.constructor(
                        CommentPreViewDTO.class,
                        reviewEntity.user.nickname,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct()))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .leftJoin(reviewLikeEntity).on(reviewLikeEntity.review.eq(reviewEntity))
                .leftJoin(reviewDislikeEntity).on(reviewDislikeEntity.review.eq(reviewEntity))
                .where(reviewEntity.parentReview.id.eq(parentReviewId))
                .groupBy(reviewEntity.id, reviewEntity.user.nickname, reviewEntity.content, reviewEntity.createdAt)
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
