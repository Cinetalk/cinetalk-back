package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.rate.entity.QRateEntity;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review.dto.ReviewResponseDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final getNewMovie getNewMovie;
    private final MovieRepository movieRepository;
    private final CallAPI callAPI;
    public final JWTUtil jwtUtil;
    private final JPAQueryFactory queryFactory;
    private final ReviewRepository reviewRepository;

    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QRateEntity rate = QRateEntity.rateEntity;

    public List<Map<String, Object>> nowPlayingList() throws IOException {

        MovieEntity time = movieRepository.findFirstByOrderByCreatedAtAsc();

        LocalDate createdAt = time.getCreatedAt().toLocalDate();
        LocalDate nowDate = LocalDate.now();

        Duration duration = Duration.between(createdAt.atStartOfDay(), nowDate.atStartOfDay());
        long days = duration.toDays();

        List<Map<String, Object>> result = new ArrayList<>();

        log.info("diffOfDay: " + days);

        if (days > 6) {

            movieRepository.deleteAll();

            List<Map<String, Object>> list = getNewMovie.MainList();

            for (Map<String, Object> info : list) {

                Map<String, Object> map = getOneByName((String) info.get("movieNm"));

                if (map != null) {

                    MovieDTO movieDTO = new MovieDTO();

                    int movieid = (int) map.get("id");
                    movieDTO.setMovieId(Long.valueOf(movieid));
                    movieDTO.setMovienm((String) map.get("title"));
                    movieDTO.setAudiAcc(Integer.parseInt((String) info.get("audiAcc")));

                    MovieEntity movieEntity = MovieEntity.ToMovieEntity(movieDTO);

                    movieRepository.save(movieEntity);

                    result.add(map);
                }
            }
        } else {

            List<MovieEntity> list = movieRepository.findAll();

            for (MovieEntity movieEntity : list) {
                Long movieId = movieEntity.getMovieId();

                result.add(getOneByID(movieId));
            }
        }
        return result;
    }

    public Map<String, Object> getOneByName(String query) throws IOException {

        log.info("query : " + query);

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query=" + query;

        Map<String, Object> responsebody = callAPI.callAPI(url);


        List<Map<String, Object>> resultList = (List<Map<String, Object>>) responsebody.get("results");

        if (!resultList.isEmpty()) {

            return resultList.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Object> getOneByID(Long movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?language=ko";

        return callAPI.callAPI(url);
    }

    public List<Map<String, Object>> ReviewByUser(HttpServletRequest request) throws IOException {

        String accessToken = request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);

        NumberTemplate<Long> RereviewCountSubquery = Expressions.numberTemplate(Long.class,
                "(select count(*) from ReviewEntity where parentReview.id = {0})", review.id);


        List<Tuple> result = queryFactory
                .select(review,
                        //JPAExpressions.select(review.count()).from(review).where(review.id.eq(review.parentReview.id)),
                        RereviewCountSubquery.as("RereivewCount"),
                        JPAExpressions.select(rate.count()).from(rate).where(rate.review.id.eq(review.id))
                )
                .from(review)
                .leftJoin(user).on(review.user.eq(user))
                .where(user.email.eq(email).and(review.parentReview.id.isNull()))
                .orderBy(review.createdAt.asc())
                .fetch();

        List<Map<String, Object>> resultlist = new ArrayList<>();

        for (Tuple tuple : result) {

            Map<String, Object> resultMap = new HashMap<>();

            ReviewDTO reviewDTO = ReviewDTO.toReviewDTO(Objects.requireNonNull(tuple.get(review)));

            Long reReviewCount = tuple.get(1, Long.class);
            Long rateCount = tuple.get(2, Long.class);

            Map<String, Object> oneByID = getOneByID(reviewDTO.getMovieId());
            String poster = (String) oneByID.get("poster_path");

            resultMap.put("review_id", reviewDTO.getId());
            resultMap.put("movie_id", reviewDTO.getMovieId());
            resultMap.put("user_id", reviewDTO.getUser().getId());
            resultMap.put("star", reviewDTO.getStar());
            resultMap.put("content", reviewDTO.getContent());
            resultMap.put("reReviewCount", reReviewCount);
            resultMap.put("likeCount", rateCount);
            resultMap.put("poster", poster);
            resultlist.add(resultMap);
        }

        return resultlist;
    }

    public List<Map<String, Object>> HidingPiece() throws IOException {

        List<Tuple> movielist = queryFactory
                .select(review.count(), review.movieId)
                .from(review)
                .groupBy(review.movieId)
                .orderBy(review.count().desc())
                .orderBy(review.movieId.asc())
                .limit(10)
                .fetch();

        List<Map<String, Object>> resultlist = new ArrayList<>();



        for (Tuple tuple : movielist) {

            Long movieid = tuple.get(1, Long.class);



            NumberTemplate<Long> rateCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from RateEntity where rate = 1 and review.id = {0})", review.id);

            NumberTemplate<Long> reReviewCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from ReviewEntity where parentReview.id = {0})", review.id);

            NumberTemplate<Double> avgStarSubquery = Expressions.numberTemplate(Double.class,
                    "(select ROUND(avg(star), 1) from ReviewEntity where movieId = {0} and parentReview.id is null)", movieid);

            Tuple result = queryFactory
                    .select(review,
                            rateCountSubquery.as("rateCount"),
                            reReviewCountSubquery.as("reReviewCount"),
                            avgStarSubquery.as("avgStar"))
                    .from(review)
                    .where(review.movieId.eq(movieid).and(review.parentReview.isNull()))
                    .orderBy(rateCountSubquery.desc())
                    .limit(1)
                    .fetchFirst();

            Map<String, Object> oneByID = getOneByID(movieid);

            ReviewEntity reviewEntity = result.get(review);

            LocalDateTime createdAt = result.get(review).getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
            String formattedDate = createdAt.format(formatter);

            Map<String, Object> map = new HashMap<>();

            map.put("movieid",movieid);
            map.put("star",reviewEntity.getStar());
            map.put("content",reviewEntity.getContent());
            map.put("regDate", formattedDate);
            map.put("likeCount", result.get(1, Long.class));
            map.put("rereviewCount", result.get(2, Long.class));
            map.put("StarAvg", result.get(3, Double.class));
            map.put("movieposter", "https://image.tmdb.org/t/p/original" + (String) oneByID.get("poster_path"));

            resultlist.add(map);
        }

        return resultlist;
    }

    public ResponseEntity<?> MentionKeyword() {

        // 오늘 날짜 설정
        LocalDate currentDate = LocalDate.now();

        log.info("RegDate :" + currentDate);

        // 오늘 자 리뷰 가져오기
        List<String> reviewList = queryFactory
                .select(review.content)
                .from(review)
                // review.createdAt의 날짜 부분만 비교하기 위해 LocalDate로 변환 후 비교
                .where(review.createdAt.between(currentDate.atStartOfDay(), currentDate.atTime(LocalTime.MAX)).and(review.parentReview.isNull()))
                .fetch();

        if(reviewList.isEmpty()){

            return new ResponseEntity<>("",HttpStatus.OK);
        }

        StringBuilder Review = new StringBuilder();
        //리뷰 직렬화
        for (String content : reviewList) {
            Review.append(content);
        }

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        // 형태소 분석 및 단어 추출 - 알고리즘 최적화
        Map<String, Integer> wordFrequency = new HashMap<>();

        List<Token> tokenList = komoran.analyze(String.valueOf(Review)).getTokenList();

        for (Token token : tokenList) {
            String pos = token.getPos();
            String morph = token.getMorph();
            if (pos.contains("NN") && morph.length() > 1) {
                // 단어가 이미 존재하면 빈도수를 증가시키고, 없으면 새로운 키로 추가
                wordFrequency.put(morph, wordFrequency.getOrDefault(morph, 0) + 1);
            }
        }

        // 빈도순으로 정렬
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFrequency.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));


        List<Map<String, Object>> resultList = new ArrayList<>();

        for (int i = 0; i<5; i++) {

            Map.Entry<String, Integer> entry = sortedList.get(i);

            String keyword = entry.getKey();

            List<ReviewEntity> list = reviewRepository.findTop10ByContentContainingAndParentReviewIsNullOrderByCreatedAtAsc(keyword);

            List<Map<String,Object>> result = new ArrayList<>();

            for (ReviewEntity entity:list){

                Map<String,Object> map = new HashMap<>();

                ReviewDTO reviewDTO = ReviewDTO.toReviewDTO(entity);
                map.put("review",reviewDTO);

                String nickname = UserDTO.ToUserDTO(entity.getUser()).getNickname();
                map.put("nickname",nickname);

                result.add(map);
            }

            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("keyword", keyword);
            resultMap.put("reviewList", result);
            resultList.add(resultMap);
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public long TotalReviewCount(){

        return reviewRepository.count();
    }
}
