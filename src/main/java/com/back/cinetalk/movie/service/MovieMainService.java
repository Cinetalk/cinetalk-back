package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.rate.entity.QRateEntity;
import com.back.cinetalk.rereview.entity.QReReviewEntity;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QReReviewEntity reReview = QReReviewEntity.reReviewEntity;
    QRateEntity rate = QRateEntity.rateEntity;

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
            
            log.info("resultList.get(0) = " + resultList.get(0));
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

        List<Tuple> result = queryFactory
                .select(review,
                        JPAExpressions.select(reReview.count()).from(reReview).where(reReview.review_id.eq(review.id.intValue())),
                        JPAExpressions.select(rate.count()).from(rate).where(rate.review_id.eq(review.id.intValue()))
                )
                .from(review)
                .leftJoin(user).on(review.user_id.eq(user.id.intValue()))
                .where(user.email.eq(email))
                .orderBy(review.createdAt.asc())
                .fetch();

        List<Map<String,Object>> resultlist = new ArrayList<>();

        for (Tuple tuple : result) {

            Map<String,Object> resultMap = new HashMap<>();

            ReviewDTO reviewDTO = ReviewDTO.ToReviewDTO(tuple.get(review));
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

    public List<Map<String,Object>> HidingPiece() throws IOException {

        List<Tuple> movielist = queryFactory
                .select(review.count(),review.movie_id)
                .from(review)
                .groupBy(review.movie_id)
                .orderBy(review.count().desc())
                .orderBy(review.movie_id.asc())
                .limit(10)
                .fetch();

        List<Map<String,Object>> resultlist = new ArrayList<>();

        for (Tuple tuple : movielist) {

            int movieid = tuple.get(1,Integer.class);

            NumberTemplate<Long> rateCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from RateEntity where rate = 1 and review_id = {0})", review.id);

            NumberTemplate<Long> reReviewCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from ReReviewEntity where review_id = {0})", review.id);

            NumberTemplate<Double> avgStarSubquery = Expressions.numberTemplate(Double.class,
                    "(select ROUND(avg(star), 1) from ReviewEntity where movie_id = {0})", movieid);

            Tuple result = queryFactory
                    .select(review,
                            rateCountSubquery.as("rateCount"),
                            reReviewCountSubquery.as("reReviewCount"),
                            avgStarSubquery.as("avgStar"))
                    .from(review)
                    .where(review.movie_id.eq(movieid))
                    .orderBy(rateCountSubquery.desc())
                    .limit(1)
                    .fetchFirst();

            ReviewDTO reviewDTO = ReviewDTO.ToReviewDTO(result.get(review));
            Map<String, Object> oneByID = getOneByID(String.valueOf(movieid));

            Map<String,Object> map = new HashMap<>();

            LocalDateTime createdAt = result.get(review).getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            String formattedDate = createdAt.format(formatter);

            map.put("reviewData",reviewDTO);
            map.put("regDate",formattedDate);
            map.put("likeCount",result.get(1,Long.class));
            map.put("rereviewCount",result.get(2,Long.class));
            map.put("StarAvg",result.get(3,Double.class));
            map.put("movieposter","https://image.tmdb.org/t/p/original"+(String)oneByID.get("poster_path"));

            resultlist.add(map);
        }

        return resultlist;
    }
}
