package com.back.cinetalk.review.repository;

import com.back.cinetalk.badge.entity.QBadgeEntity;
import com.back.cinetalk.rate.dislike.entity.QReviewDislikeEntity;
import com.back.cinetalk.rate.like.entity.QReviewLikeEntity;
import com.back.cinetalk.review.dto.CommentPreViewDTO;
import com.back.cinetalk.review.dto.QReviewPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.userBadge.entity.QUserBadgeEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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

    private final QReviewEntity reviewEntity = QReviewEntity.reviewEntity;
    private final QUserEntity userEntity = QUserEntity.userEntity;
    private final QReviewLikeEntity reviewLikeEntity = QReviewLikeEntity.reviewLikeEntity;
    private final QReviewDislikeEntity reviewDislikeEntity = QReviewDislikeEntity.reviewDislikeEntity;
    private final QUserBadgeEntity userBadgeEntity = QUserBadgeEntity.userBadgeEntity;
    private final QBadgeEntity badgeEntity = QBadgeEntity.badgeEntity;

    @Override
    public Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Long userId, Pageable pageable, String sortType) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = getSortOrder(sortType);

        List<ReviewPreViewDTO> results = queryFactory
                .select(new QReviewPreViewDTO(
                        reviewEntity.id,
                        reviewEntity.user.nickname,
                        reviewEntity.user.profile,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct(),
                        JPAExpressions.select(reviewEntity.count())
                                .from(reviewEntity)
                                .where(reviewEntity.parentReview.eq(reviewEntity)),
                        // 사용자가 이 리뷰에 좋아요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewLikeEntity)
                                .where(reviewLikeEntity.review.eq(reviewEntity))
                                .where(reviewLikeEntity.user.id.eq(userId))
                                .exists(),  // 좋아요 여부 확인
                        // 사용자가 이 리뷰에 싫어요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewDislikeEntity)
                                .where(reviewDislikeEntity.review.eq(reviewEntity))
                                .where(reviewDislikeEntity.user.id.eq(userId))
                                .exists()  // 싫어요 여부 확인
                ))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .leftJoin(reviewLikeEntity).on(reviewLikeEntity.review.eq(reviewEntity))
                .leftJoin(reviewDislikeEntity).on(reviewDislikeEntity.review.eq(reviewEntity))
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .groupBy(reviewEntity.id)
                .orderBy(orderSpecifier)  // 정렬 기준 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 자식 리뷰(댓글) 개수 추가
        insertCommentCount(results, queryFactory);
        insertGenreList(results, queryFactory);

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
    public Page<CommentPreViewDTO> findAllByParentReviewId(Long parentReviewId, Long userId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<CommentPreViewDTO> results = queryFactory
                .select(Projections.constructor(
                        CommentPreViewDTO.class,
                        reviewEntity.id,
                        reviewEntity.user.nickname,
                        reviewEntity.user.profile,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct(),
                        // 사용자가 이 리뷰에 좋아요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewLikeEntity)
                                .where(reviewLikeEntity.review.eq(reviewEntity))
                                .where(reviewLikeEntity.user.id.eq(userId))
                                .exists(),  // 좋아요 여부 확인
                        // 사용자가 이 리뷰에 싫어요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewDislikeEntity)
                                .where(reviewDislikeEntity.review.eq(reviewEntity))
                                .where(reviewDislikeEntity.user.id.eq(userId))
                                .exists()  // 싫어요 여부 확인
                ))
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

    @Override
    public List<ReviewPreViewDTO> findBestReviews(Long movieId, Long userId, int limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<ReviewPreViewDTO> bestReviewList = queryFactory
                .select(new QReviewPreViewDTO(
                        reviewEntity.id,
                        reviewEntity.user.nickname,
                        reviewEntity.user.profile,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct(),
                        JPAExpressions.select(reviewEntity.count())
                                .from(reviewEntity)
                                .where(reviewEntity.parentReview.eq(reviewEntity)),
                        JPAExpressions.selectOne()
                                .from(reviewLikeEntity)
                                .where(reviewLikeEntity.review.eq(reviewEntity))
                                .where(reviewLikeEntity.user.id.eq(userId))
                                .exists(),  // 좋아요 여부 확인
                        // 사용자가 이 리뷰에 싫어요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewDislikeEntity)
                                .where(reviewDislikeEntity.review.eq(reviewEntity))
                                .where(reviewDislikeEntity.user.id.eq(userId))
                                .exists()  // 싫어요 여부 확인
                ))
                .from(reviewEntity)
                .leftJoin(reviewLikeEntity).on(reviewLikeEntity.review.eq(reviewEntity))
                .leftJoin(reviewDislikeEntity).on(reviewDislikeEntity.review.eq(reviewEntity))
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .groupBy(reviewEntity.id)
                .having(reviewLikeEntity.countDistinct().goe(10))
                .having(reviewLikeEntity.countDistinct().divide(reviewDislikeEntity.countDistinct().add(1)).goe(0.5))
                .orderBy(reviewLikeEntity.countDistinct().desc())
                .limit(limit)
                .fetch();

        insertCommentCount(bestReviewList, queryFactory);
        insertGenreList(bestReviewList, queryFactory);

        return bestReviewList;
    }

    @Override
    public Page<ReviewPreViewDTO> findGeneralReviewsExcludingBest(Long movieId, Long userId, List<Long> bestReviewIds, Pageable pageable, String sortType) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        // 정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = getSortOrder(sortType);

        List<ReviewPreViewDTO> generalReviewList = queryFactory
                .select(new QReviewPreViewDTO(
                        reviewEntity.id,
                        reviewEntity.user.nickname,
                        reviewEntity.user.profile,
                        reviewEntity.star,
                        reviewEntity.content,
                        reviewEntity.createdAt,
                        reviewEntity.spoiler,
                        reviewLikeEntity.countDistinct(),
                        reviewDislikeEntity.countDistinct(),
                        JPAExpressions.select(reviewEntity.count())
                                .from(reviewEntity)
                                .where(reviewEntity.parentReview.eq(reviewEntity)),
                        JPAExpressions.selectOne()
                                .from(reviewLikeEntity)
                                .where(reviewLikeEntity.review.eq(reviewEntity))
                                .where(reviewLikeEntity.user.id.eq(userId))
                                .exists(),  // 좋아요 여부 확인
                        // 사용자가 이 리뷰에 싫어요를 눌렀는지 여부 확인
                        JPAExpressions.selectOne()
                                .from(reviewDislikeEntity)
                                .where(reviewDislikeEntity.review.eq(reviewEntity))
                                .where(reviewDislikeEntity.user.id.eq(userId))
                                .exists()  // 싫어요 여부 확인
                ))
                .from(reviewEntity)
                .leftJoin(userEntity).on(reviewEntity.user.eq(userEntity))
                .leftJoin(reviewLikeEntity).on(reviewLikeEntity.review.eq(reviewEntity))
                .leftJoin(reviewDislikeEntity).on(reviewDislikeEntity.review.eq(reviewEntity))
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .where(reviewEntity.id.notIn(bestReviewIds))
                .groupBy(reviewEntity.id)
                .orderBy(orderSpecifier)  // 정렬 기준 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 자식 리뷰(댓글) 개수 추가
        insertCommentCount(generalReviewList, queryFactory);
        insertGenreList(generalReviewList, queryFactory);

        Long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(reviewEntity.movieId.eq(movieId))
                .where(reviewEntity.parentReview.isNull())
                .where(reviewEntity.id.notIn(bestReviewIds))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(generalReviewList, pageable, total);
    }

    private void insertCommentCount(List<ReviewPreViewDTO> reviewList, JPAQueryFactory queryFactory) {
        for (ReviewPreViewDTO review : reviewList) {
            // 각 리뷰에 대한 자식 리뷰(댓글)의 개수를 세는 쿼리
            Long commentCount = queryFactory
                    .select(reviewEntity.count())
                    .from(reviewEntity)
                    .where(reviewEntity.parentReview.id.eq(review.getId())) // 부모 리뷰 ID가 해당 리뷰 ID인 경우
                    .fetchOne();

            // commentCount가 null일 경우 0으로 설정
            if (commentCount == null) {
                commentCount = 0L;
            }

            // DTO에 commentCount를 설정
            review.setCommentCount(commentCount.intValue());
        }
    }


    private void insertGenreList(List<ReviewPreViewDTO> generalReviewList, JPAQueryFactory queryFactory) {
        for (ReviewPreViewDTO dto : generalReviewList) {
            List<String> badgeList = queryFactory
                    .select(badgeEntity.name)
                    .from(userBadgeEntity)
                    .join(userBadgeEntity.badge, badgeEntity)
                    .where(userBadgeEntity.user.nickname.eq(dto.getNickName())
                            .and(userBadgeEntity.isUse.isTrue()))
                    .fetch();
            dto.setBadgeList(badgeList);
        }
    }

    // 정렬 기준 설정 메서드
    private OrderSpecifier<?> getSortOrder(String sortType) {
        if ("star".equalsIgnoreCase(sortType)) {
            return reviewEntity.star.desc();  // 별점순 내림차순 정렬
        } else {
            return reviewEntity.createdAt.desc();  // 최신순 정렬
        }
    }
}
