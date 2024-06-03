package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.movie.dto.ReviewByUserDTO;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.rate.entity.QRateEntity;
import com.back.cinetalk.rereview.entity.QReReviewEntity;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final getNewMovie getNewMovie;
    private final MovieRepository movieRepository;
    private final CallAPI callAPI;
    public final JWTUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    public List<Map<String, Object>> nowPlayingList() throws IOException {

        LocalDate today = LocalDate.now();

        List<MovieEntity> lists = movieRepository.findByCreatedAt(today.atStartOfDay());

        List<Map<String, Object>> result = new ArrayList<>();

        if (lists.isEmpty()) {

            List<String> nowPlayingName = getNewMovie.MainList();
            for (String query : nowPlayingName) {

                Map<String, Object> map = getOneByName(query);

                if (map != null) {

                    MovieDTO movieDTO = new MovieDTO();

                    movieDTO.setMovie_id(String.valueOf(map.get("id")));
                    movieDTO.setMovienm((String) map.get("title"));

                    MovieEntity movieEntity = MovieEntity.ToMovieEntity(movieDTO);

                    movieRepository.save(movieEntity);

                    result.add(map);
                }
            }
            //map.put("poster_path","https://image.tmdb.org/t/p/w500"+poster_path);
        } else {

            for (MovieEntity movieEntity : lists) {
                result.add(getOneByID(movieEntity.getMovie_id()));
            }
        }

        return result;
    }

    public Map<String, Object> getOneByName(String query) throws IOException {

        log.info("query : " + query);

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query=" + query;

        Map<String, Object> responsebody = callAPI.callAPI(url);

        log.info("responsebody = " + responsebody);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) responsebody.get("results");

        log.info("resultList = " + resultList);
        if (!resultList.isEmpty()) {
            System.out.println("resultList.get(0) = " + resultList.get(0));
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Object> getOneByID(String movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?language=ko";

        return callAPI.callAPI(url);
    }

    public List<Map<String,Object>> ReviewByUser(HttpServletRequest request) throws IOException {

        String accessToken= request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);

        QReviewEntity review = QReviewEntity.reviewEntity;
        QUserEntity user = QUserEntity.userEntity;
        QReReviewEntity reReview = QReReviewEntity.reReviewEntity;
        QRateEntity rate = QRateEntity.rateEntity;

        List<Tuple> result = queryFactory
                .select(review,
                        JPAExpressions.select(reReview.count()).from(reReview).where(reReview.review_id.eq(review.id.intValue())),
                        JPAExpressions.select(rate.count()).from(rate).where(rate.review_id.eq(review.id.intValue()))
                )
                .from(review)
                .leftJoin(user).on(review.user_id.eq(user.id.intValue()))
                .where(user.email.eq(email))
                .orderBy(review.regdate.asc())
                .fetch();

        List<Map<String,Object>> resultlist = new ArrayList<>();

        for (Tuple tuple : result) {

            Map<String,Object> resultMap = new HashMap<>();

            ReviewDTO reviewDTO = ReviewDTO.ToReviewDTO(tuple.get(review));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            String formattedDate = reviewDTO.getRegdate().format(formatter);
            Long reReviewCount = tuple.get(1, Long.class);
            Long rateCount = tuple.get(2, Long.class);
            Map<String, Object> oneByID = getOneByID(String.valueOf(reviewDTO.getMovie_id()));
            String poster = (String) oneByID.get("poster_path");

            resultMap.put("review_id",reviewDTO.getId());
            resultMap.put("movie_id",reviewDTO.getMovie_id());
            resultMap.put("user_id",reviewDTO.getUser_id());
            resultMap.put("star",reviewDTO.getStar());
            resultMap.put("content",reviewDTO.getContent());
            resultMap.put("reReviewCount",reReviewCount);
            resultMap.put("likeCount",rateCount);
            resultMap.put("poster",poster);

            resultlist.add(resultMap);
        }

        return resultlist;
    }
}
