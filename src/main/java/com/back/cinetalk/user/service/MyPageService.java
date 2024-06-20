package com.back.cinetalk.user.service;

import com.back.cinetalk.bookmark.entity.QBookmarkEntity;
import com.back.cinetalk.user.dto.BadgeByUserResponseDTO;
import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.badge.repository.BadgeRepository;
import com.back.cinetalk.rate.entity.QRateEntity;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.back.cinetalk.bookmark.entity.QBookmarkEntity.*;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final JWTUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QRateEntity rate = QRateEntity.rateEntity;
    QBookmarkEntity bookmark = QBookmarkEntity.bookmarkEntity;

    public ResponseEntity<?> BadgeByUser(HttpServletRequest request){

        String access = request.getHeader("access");

        String email = jwtUtil.getEmail(access);

        UserEntity byEmail = userRepository.findByEmail(email);

        List<BadgeEntity> byuser = badgeRepository.findByUser(byEmail);

        List<BadgeByUserResponseDTO> result = new ArrayList<>();

        for (BadgeEntity badgeEntity:byuser) {

            BadgeByUserResponseDTO badge = BadgeByUserResponseDTO.builder()
                    .badge_name(badgeEntity.getGenre().getBadgename())
                    .genre_name(badgeEntity.getGenre().getName())
                    .useyn(badgeEntity.getUseyn())
                    .build();

            result.add(badge);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<?> CountSumByUser(HttpServletRequest request){

        String access = request.getHeader("access");

        String email = jwtUtil.getEmail(access);

        UserEntity byEmail = userRepository.findByEmail(email);

        Long rateCount = queryFactory.select(rate.count())
                .from(rate)
                .where(rate.review.user.id.eq(byEmail.getId()).and(rate.rate.eq(1)))
                .fetchFirst();

        Long reviewCount = queryFactory.select(review.count())
                .from(review)
                .where(review.user.id.eq(byEmail.getId()))
                .fetchFirst();

        Long bookmarkCount = queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.user.id.eq(byEmail.getId()))
                .fetchFirst();



        return null;
    }
}
