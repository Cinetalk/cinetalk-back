




package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ReviewPreViewDTO> findAllByMovieIdWithUser(Long movieId, Pageable pageable) {
        QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
        QUserEntity userEntity = QUserEntity.userEntity;

        JPQLQuery<ReviewPreViewDTO> query = new JPAQuery<>(entityManager);

        List<ReviewPreViewDTO> results = query
                .select(Projections.constructor(
                        ReviewPreViewDTO.class,
                        userEntity.nickname,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.userId.eq(userEntity.id))
                .where(reviewEntity.movieId.eq(movieId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
