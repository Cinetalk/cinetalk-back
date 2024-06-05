package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.rate.entity.QRateEntity;
import com.back.cinetalk.rereview.entity.QReReviewEntity;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.querydsl.core.Tuple;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final getNewMovie getNewMovie;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final CallAPI callAPI;
    public final JWTUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QReReviewEntity reReview = QReReviewEntity.reReviewEntity;
    QRateEntity rate = QRateEntity.rateEntity;

    public List<Map<String, Object>> nowPlayingList() throws IOException {


        MovieEntity time = movieRepository.findFirstByOrderByCreatedAtAsc();

        LocalDate createdAt = time.getCreatedAt().toLocalDate();
        LocalDate nowDate = LocalDate.now();

        Duration duration = Duration.between(createdAt.atStartOfDay(), nowDate.atStartOfDay());
        long days = duration.toDays();

        List<Map<String, Object>> result = new ArrayList<>();

        log.info("diffOfDay: "+days);

        if(days>6){

            movieRepository.deleteAll();

            List<Map<String,Object>> list = getNewMovie.MainList();

            for (Map<String,Object> info:list) {

                Map<String, Object> map = getOneByName((String) info.get("movieNm"));

                if (map != null) {

                    MovieDTO movieDTO = new MovieDTO();

                    movieDTO.setMovie_id((Integer) map.get("id"));
                    movieDTO.setMovienm((String) map.get("title"));
                    movieDTO.setAudiAcc(Integer.parseInt((String)info.get("audiAcc")));

                    MovieEntity movieEntity = MovieEntity.ToMovieEntity(movieDTO);

                    movieRepository.save(movieEntity);

                    result.add(map);
                }
            }
        }else{

            List<MovieEntity> list = movieRepository.findAll();

            for (MovieEntity movieEntity:list) {
                int movieId = movieEntity.getMovie_id();

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

    public Map<String, Object> getOneByID(int movie_id) throws IOException {

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
            Map<String, Object> oneByID = getOneByID(reviewDTO.getMovie_id());
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
            Map<String, Object> oneByID = getOneByID(movieid);

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

    public List<Map<String,Object>> MentionKeword(){

        String strToAnalyze = "인생이 야발 준나게 인생이 아주 그냥 인생이 아오 웡카쩔어 ㅋㅋㅋㅋㅋ그랭그랭 엉엉 담에보자인생웡카 어쩌구 복합명사는 인생은 웡카가.";

        String strToAnalyze1 = "웡카 라는 영화에 대해서 설명해보겠습니다.웡카는 진짜 존나 재미없어요.나는 시팔 찰리의 초콜릿공장같은걸 원했는데 시팔 이건 그냥 애들 쳐보라고 만든 영화잖아요.";


        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        // 형태소 분석 후 리스트 생성
        List<Token> tokenList = komoran.analyze(strToAnalyze1).getTokenList();

        Map<String, Integer> morphPosCountMap = new HashMap<>();

        //단어선정 및 개수 세는 작업
        for (Token token : tokenList) {
            String pos = token.getPos();
            String morph = token.getMorph();

            if (pos.contains("NN") && morph.length() > 1) {
                String key = morph + "/" + pos;
                morphPosCountMap.put(key, morphPosCountMap.getOrDefault(key, 0) + 1);
            }
        }

        // Map을 많이 나온 단어갯수 기준으로 내림차순 정렬
        List<Map.Entry<String, Integer>> sortedEntries = morphPosCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();


        List<Map<String,Object>> resultlist = new ArrayList<>();
        // 정렬된 Map으로 returnMap 구성

        for (Map.Entry<String, Integer> entry : sortedEntries) {

            String morph = entry.getKey().split("/")[0];
            log.info(morph);



        }



        return null;
    }
}
