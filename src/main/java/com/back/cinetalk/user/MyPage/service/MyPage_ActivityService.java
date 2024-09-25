package com.back.cinetalk.user.MyPage.service;

import com.back.cinetalk.bookmark.entity.QBookmarkEntity;
import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.damage.entity.DamageEntity;
import com.back.cinetalk.damage.entity.QDamageEntity;
import com.back.cinetalk.genre.entity.QGenreEntity;
import com.back.cinetalk.keyword.entity.QKeywordEntity;
import com.back.cinetalk.movie.service.MovieMainService;
import com.back.cinetalk.rate.like.entity.QReviewLikeEntity;
import com.back.cinetalk.rate.like.repository.ReviewLikeRepository;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.reviewGenre.entity.QReviewGenreEntity;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.MyPage.dto.activity.*;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.userBadge.entity.QUserBadgeEntity;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import com.back.cinetalk.userBadge.repository.UserBadgeRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MyPage_ActivityService {

    private final UserBadgeRepository userBadgeRepository;
    private final JPAQueryFactory queryFactory;
    private final ReviewRepository reviewRepository;
    private final MovieMainService movieMainService;
    private final UserByAccess userByAccess;
    private final ReviewLikeRepository reviewLikeRepository;

    QReviewEntity review = QReviewEntity.reviewEntity;
    QBookmarkEntity bookmark = QBookmarkEntity.bookmarkEntity;
    QKeywordEntity keyword = QKeywordEntity.keywordEntity;
    QReviewGenreEntity reviewGenre = QReviewGenreEntity.reviewGenreEntity;
    QGenreEntity genre = QGenreEntity.genreEntity;
    QUserBadgeEntity userBadge = QUserBadgeEntity.userBadgeEntity;
    QReviewLikeEntity reviewLike = QReviewLikeEntity.reviewLikeEntity;
    QDamageEntity damage = QDamageEntity.damageEntity;

    //TODO 유저의 뱃지 목록
    @Transactional(readOnly = true)
    public ResponseEntity<?> BadgeByUser(HttpServletRequest request){

        UserEntity byEmail = userByAccess.getUserEntity(request);

        List<UserBadgeEntity> byUser = userBadgeRepository.findByUser(byEmail);

        List<BadgeByUserResponseDTO> result = new ArrayList<>();

        for (UserBadgeEntity userBadgeEntity: byUser) {

            BadgeByUserResponseDTO badge = BadgeByUserResponseDTO.builder()
                .genre_id(userBadgeEntity.getBadge().getGenre().getId())
                .genre_name(userBadgeEntity.getBadge().getGenre().getName())
                .badge_name(userBadgeEntity.getBadge().getName())
                .isUse(userBadgeEntity.isUse())
                .build();

            result.add(badge);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //TODO 유저의 장르별 댓글 갯수
    @Transactional(readOnly = true)
    public ResponseEntity<?> ReviewByGenreFromUser(HttpServletRequest request){

        UserEntity byEmail = userByAccess.getUserEntity(request);

        List<ReviewByGenreFromUserDTO> result = queryFactory
                .select(Projections.constructor(ReviewByGenreFromUserDTO.class, genre.id, genre.name, reviewGenre.count().coalesce(0L).as("count")))
                .from(genre)
                .leftJoin(reviewGenre).on(genre.id.eq(reviewGenre.genre.id).and(reviewGenre.review.user.eq(byEmail)))
                .groupBy(genre.id)
                .fetch();

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //TODO 뱃지 장착,해제 처리
    @Transactional
    public StateRes BadgeUseUpdate(HttpServletRequest request,List<Long> BadgeList){

        UserEntity byEmail = userByAccess.getUserEntity(request);

        queryFactory.update(userBadge).set(userBadge.isUse,false)
                .where(userBadge.user.eq(byEmail))
                .execute();

        queryFactory.update(userBadge).set(userBadge.isUse,true)
                .where(userBadge.user.eq(byEmail)
                        .and(userBadge.badge.genre.id.in(BadgeList)))
                .execute();

        return new StateRes(true);
    }

    //TODO 유저의 좋아요, 댓글, 찜 갯수 모음
    @Transactional(readOnly = true)
    public ResponseEntity<?> CountSumByUser(HttpServletRequest request){

        UserEntity byEmail = userByAccess.getUserEntity(request);

        Long rateCount = queryFactory.select(reviewLike.count())
                .from(reviewLike)
                .where(reviewLike.review.user.eq(byEmail))
                .fetchFirst();

        Long reviewCount = queryFactory.select(review.count())
                .from(review)
                .where(review.user.id.eq(byEmail.getId()))
                .fetchFirst();

        Long bookmarkCount = queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.user.id.eq(byEmail.getId()))
                .fetchFirst();

        CountSumByUserResponseDTO result = CountSumByUserResponseDTO.builder()
            .rateCount(rateCount)
            .reviewCount(reviewCount)
            .bookmarkCount(bookmarkCount)
            .build();

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //TODO 유저의 최근 댓글
    @Transactional(readOnly = true)
    public ResponseEntity<?> ReviewByUser(String sort,HttpServletRequest request) throws IOException {

        UserEntity user = userByAccess.getUserEntity(request);

        List<ReviewEntity> reviewList = new ArrayList<>();

        if(sort != null && sort.equals("desc")){
            reviewList = reviewRepository.findByUserAndParentReviewIsNullOrderByCreatedAtDesc(user);
        }else{
            reviewList = reviewRepository.findByUserAndParentReviewIsNullOrderByCreatedAt(user);
        }

        List<ReviewByUserResponseDTO> result = new ArrayList<>();

        for (ReviewEntity reviewEntity: reviewList) {

            int RereviewCont = reviewRepository.countByParentReview(reviewEntity);

            long rateCount = reviewLikeRepository.countByReviewId(reviewEntity.getId());

            Map<String, Object> oneByID = movieMainService.getOneByID(reviewEntity.getMovieId());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

            String regDate = reviewEntity.getCreatedAt().format(formatter);

            ReviewByUserResponseDTO responseDTO = ReviewByUserResponseDTO.builder()
                    .review_id(reviewEntity.getId())
                    .movie_id(reviewEntity.getMovieId())
                    .movienm((String) oneByID.get("title"))
                    .poster_id("https://image.tmdb.org/t/p/original"+oneByID.get("poster_path"))
                    .star(reviewEntity.getStar())
                    .content(reviewEntity.getContent())
                    .RateCount(rateCount)
                    .RereviewCount(RereviewCont)
                    .regDate(regDate)
                    .build();

            result.add(responseDTO);
        }
        if(sort != null && sort.equals("like")){
            result.sort(Comparator.comparingLong(ReviewByUserResponseDTO::getRateCount).reversed());
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //TODO 유저의 평가(키워드,댓글) 날짜별 로그
    @Transactional(readOnly = true)
    public ResponseEntity<?> LogByUser(String sort,HttpServletRequest request) throws IOException {

        UserEntity userEntity = userByAccess.getUserEntity(request);


        List<Tuple> reviewResult = queryFactory.
                select(Expressions.constant("review"),review.id,review.movieId,review.star,review.content,review.createdAt).from(review)
                .where(review.user.id.eq(userEntity.getId()).and(review.parentReview.id.isNull()))
                .fetch();

        List<Tuple> keywordResult = queryFactory.
                select(Expressions.constant("keyword"),
                        keyword.id, keyword.movieId, Expressions.nullExpression(), keyword.keyword.as("content"), keyword.createdAt)
                .from(keyword)
                .where(keyword.user.id.eq(userEntity.getId()))
                .fetch();

        List<Tuple> combinedResults = new ArrayList<>();
        combinedResults.addAll(reviewResult);
        combinedResults.addAll(keywordResult);

        if(sort != null && sort.equals("desc")){
            combinedResults.sort(Comparator.comparing((Tuple tuple) -> tuple.get(5, LocalDateTime.class)).reversed());
        }else {
            combinedResults.sort(Comparator.comparing(tuple -> tuple.get(5, LocalDateTime.class)));
        }

        List<LogByUserResponseDTO> result = new ArrayList<>();

        for (Tuple tuple : combinedResults) {

            Map<String, Object> oneByID = movieMainService.getOneByID(tuple.get(2, Long.class));

            Double value = tuple.get(3, Double.class);
            double star = (value != null) ? value : 0.0;

            LogByUserResponseDTO responseDTO = LogByUserResponseDTO.builder()
                    .category(tuple.get(0,String.class))
                    .id(tuple.get(1,Long.class))
                    .movieId(tuple.get(2,Long.class))
                    .movienm((String) oneByID.get("title"))
                    .poster_path("https://image.tmdb.org/t/p/original"+oneByID.get("poster_path"))
                    .star(star)
                    .content(tuple.get(4,String.class))
                    .createdAt(tuple.get(5,LocalDateTime.class))
                    .build();

            result.add(responseDTO);
        }

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //TODO 유저의 제재 현황
    public ResponseEntity<?> DamageByUser(HttpServletRequest request){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        LocalDate now = LocalDate.now();

        DamageByUserResponseDTO userDamage = queryFactory.select(Projections.constructor(
                DamageByUserResponseDTO.class,
                        damage.content.as("review_content"),
                        damage.category,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%y.%m.%d')",damage.startDate).as("startDate"),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%y.%m.%d')",damage.endDate).as("endDate"),
                        Expressions.numberTemplate(Long.class, "DATEDIFF({0}, {1}) + 1", damage.endDate, damage.startDate).as("date")
                        ))
                .from(damage)
                .where(damage.user.id.eq(userEntity.getId())
                        .and(damage.startDate.loe(now))
                        .and(damage.endDate.goe(now)))
                .orderBy(damage.endDate.desc())
                .limit(1)
                .fetchOne();

        if(userDamage == null){
            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(userDamage,HttpStatus.OK);
    }
}
