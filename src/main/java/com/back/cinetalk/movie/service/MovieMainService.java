package com.back.cinetalk.movie.service;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.badge.entity.QBadgeEntity;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.genre.entity.QGenreEntity;
import com.back.cinetalk.keyword.entity.QKeywordEntity;
import com.back.cinetalk.movie.dto.*;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.rate.like.entity.QReviewLikeEntity;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.reviewGenre.entity.QReviewGenreEntity;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.userBadge.entity.QUserBadgeEntity;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.back.cinetalk.review.entity.QReviewEntity.*;
import static com.back.cinetalk.user.entity.QUserEntity.*;
import static com.back.cinetalk.userBadge.entity.QUserBadgeEntity.*;

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
    private final UserByAccess userByAccess;

    QReviewEntity review = reviewEntity;
    QReviewGenreEntity reviewGenre = QReviewGenreEntity.reviewGenreEntity;
    QUserBadgeEntity userBadge = QUserBadgeEntity.userBadgeEntity;
    QKeywordEntity keyword = QKeywordEntity.keywordEntity;
    QReviewLikeEntity reviewLike = QReviewLikeEntity.reviewLikeEntity;

    //TODO movie_id 로 영화 정보 받기
    public Map<String, Object> getOneByID(Long movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?language=ko";

        return callAPI.callAPI(url);
    }

    //TODO 숨겨진 명작
    public ResponseEntity<?> HidingPiece() throws IOException {

        List<Long> fetch = queryFactory.select(review.movieId)
                .from(review)
                .groupBy(review.movieId)
                .having(review.count().between(5, 20).and(review.star.avg().goe(4)))
                .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc())
                .limit(10)
                .fetch();

        List<Map<String, Object>> resultlist = new ArrayList<>();

        for (Long movieid : fetch) {

            NumberTemplate<Long> likeCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from ReviewLikeEntity where review.id = {0})", review.id);

            NumberTemplate<Long> reReviewCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from ReviewEntity where parentReview.id = {0})", review.id);

            NumberTemplate<Double> avgStarSubquery = Expressions.numberTemplate(Double.class,
                    "(select ROUND(avg(star), 1) from ReviewEntity where movieId = {0} and parentReview.id is null)", movieid);

            Tuple result = queryFactory
                    .select(review,
                            likeCountSubquery.as("rateCount"),
                            reReviewCountSubquery.as("reReviewCount"),
                            avgStarSubquery.as("avgStar"))
                    .from(review)
                    .where(review.movieId.eq(movieid).and(review.parentReview.isNull()))
                    .orderBy(likeCountSubquery.desc())
                    .limit(1)
                    .fetchFirst();

            Map<String, Object> oneByID = getOneByID(movieid);

            if (result != null){
                ReviewEntity reviewEntity = result.get(review);

                LocalDateTime createdAt = reviewEntity.getCreatedAt();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
                String formattedDate = createdAt.format(formatter);

                Map<String, Object> map = new HashMap<>();

                map.put("movieid",movieid);
                map.put("movienm",reviewEntity.getMovienm());
                map.put("star",reviewEntity.getStar());
                map.put("content",reviewEntity.getContent());
                map.put("regDate", formattedDate);
                map.put("likeCount", result.get(1, Long.class));
                map.put("rereviewCount", result.get(2, Long.class));
                map.put("StarAvg", result.get(3, Double.class));
                map.put("movieposter", "https://image.tmdb.org/t/p/original" + (String) oneByID.get("poster_path"));

                resultlist.add(map);
            }
        }

        return new ResponseEntity<>(resultlist, HttpStatus.OK);
    }

    //TODO 자주 언급 되는 키워드
    public ResponseEntity<?> MentionKeyword() {

        // 오늘 날짜 설정
        LocalDate currentDate = LocalDate.now();

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
            Review.append(content
                    .replaceAll("\\.","")
                    .replaceAll("\\n", "")
                    .replaceAll("~","")
                    .replaceAll("\\)","")
                    .replaceAll("\\(","")
                    .replaceAll(",","")
                    .replaceAll("ㅋ","")
                    .replaceAll("ㅎ","")
                    +".");
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

                String nickname = entity.getUser().getNickname();

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

    //TODO 나와 취향이 비슷한 사람들
    public ResponseEntity<?> UserEqReviewers(HttpServletRequest request) throws IOException {

        String access = request.getHeader("access");

        NumberTemplate<Long> likeCountSubquery = Expressions.numberTemplate(Long.class,
                "(select count(*) from ReviewLikeEntity where review.id = {0})", review.id);


        // 리뷰를 많이 작성한 유저 조회
        List<UserEntity> fetch = queryFactory.select(review.user)
                .from(review)
                .where(review.parentReview.isNull())
                .groupBy(review.user)
                .orderBy(review.count().desc())
                .fetch();

        if(access == null){


        }

        // 유저가 갖고 있는 뱃지 목록 조회
        List<Long> currentUserBadgeIds = queryFactory
                .select(userBadge.badge.id)
                .from(userBadge)
                .where(userBadge.user.eq(userEntity))
                .fetch();



        if(currentUserBadgeIds.isEmpty()){

            return new ResponseEntity<>(fetch,HttpStatus.OK);
        }else{

            List<UserEntity> fetch1 = queryFactory.select(userBadge.user)
                    .from(userBadge)
                    .where(userBadge.badge.id.in(currentUserBadgeIds))
                    .groupBy(userBadge.user)
                    .fetch();

        }


        List<UserEqDTO> resultList = new ArrayList<>();


        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    //TODO 혹시 이 영화 보셨나요?
    public ResponseEntity<?> HoxyWatching(HttpServletRequest request) throws IOException {

        UserEntity userEntity = userByAccess.getUserEntity(request);

        GenreEntity genreEntity = queryFactory.select(reviewGenre.genre).from(reviewGenre)
                .where(reviewGenre.review.user.eq(userEntity).and(reviewGenre.review.parentReview.isNull()))
                .groupBy(reviewGenre.genre)
                .orderBy(reviewGenre.count().desc())
                .limit(1)
                .fetchOne();

        List<Long> movieList = new ArrayList<>();

        if(genreEntity == null){
            
            movieList = queryFactory.select(review.movieId)
                    .from(review)
                    .where(review.parentReview.isNull())
                    .groupBy(review.movieId)
                    .orderBy(review.count().desc())
                    .limit(10)
                    .fetch();
        }else{

            movieList = queryFactory
                    .select(reviewGenre.review.movieId).from(reviewGenre)
                    .where(reviewGenre.genre.id.eq(genreEntity.getId())
                            .and(reviewGenre.review.user.ne(userEntity))
                            .and(reviewGenre.review.parentReview.isNull()))
                    .groupBy(reviewGenre.review.movieId)
                    .orderBy(reviewGenre.review.count().desc())
                    .limit(10)
                    .fetch();

        }

        List<HoxyDTO> resultList = new ArrayList<>();

        for (Long  movieId: movieList) {

            Map<String, Object> oneByID = getOneByID(movieId);

            String movienm = oneByID.get("title").toString();
            String poster_path = "https://image.tmdb.org/t/p/original"+oneByID.get("poster_path").toString();

            HoxyDTO result = HoxyDTO.builder()
                    .movieId(movieId)
                    .movienm(movienm)
                    .poster_path(poster_path)
                    .build();

            resultList.add(result);
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    //TODO 메인페이지 배너
    public ResponseEntity<?> mainBanner() throws IOException {

        List<Long> MovieIdList = queryFactory.select(review.movieId).from(review)
                .where(review.parentReview.isNull()
                        .and(review.createdAt.after(LocalDateTime.now().minusDays(7).with(LocalTime.MIN))))
                .groupBy(review.movieId)
                .orderBy(review.count().desc())
                .orderBy(review.movieId.asc())
                .limit(3)
                .fetch();

        List<BannerDTO> resultList = new ArrayList<BannerDTO>();

        for(Long movieId: MovieIdList){

            Map<String, Object> oneByID = getOneByID(movieId);

            Double rate = queryFactory.select(review.star.avg())
                    .from(review)
                    .where(review.parentReview.isNull()
                            .and(review.movieId.eq(movieId)))
                    .fetchOne();

            String topKeyword = queryFactory.select(keyword.keyword)
                    .from(keyword)
                    .where(keyword.movieId.eq(movieId))
                    .groupBy(keyword.keyword)
                    .orderBy(keyword.count.desc())
                    .limit(1)
                    .fetchOne();

            List<BannerReviewDTO> ReviewList = queryFactory.select(Projections.constructor(BannerReviewDTO.class, review.star, review.content, review.createdAt))
                    .from(review)
                    .where(review.movieId.eq(movieId)
                            .and(review.parentReview.isNull()
                                    .and(review.spoiler.eq(false))))
                    .orderBy(review.createdAt.desc())
                    .limit(5)
                    .fetch();

            BannerDTO result = BannerDTO.builder()
                    .movieId(movieId)
                    .movienm(oneByID.get("title").toString())
                    .poster_path("https://image.tmdb.org/t/p/original"+oneByID.get("poster_path").toString())
                    .backdrop_path("https://image.tmdb.org/t/p/original"+oneByID.get("backdrop_path").toString())
                    .genres((List<Map<String, Object>>) oneByID.get("genres"))
                    .rate(rate)
                    .keyword(topKeyword)
                    .ReviewList(ReviewList)
                    .build();

            resultList.add(result);
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    //TODO 영화톡 TOP10
    public ResponseEntity<?> TopTenTalk(Long genreId) throws IOException {

        List<Long> movieIdList = new ArrayList<>();

        if (genreId == 0){

            movieIdList = queryFactory.select(review.movieId)
                    .from(review)
                    .where(review.parentReview.isNull()
                    .and(review.spoiler.eq(false))
                    .and(review.createdAt.after(LocalDateTime.now().minusDays(30).with(LocalTime.MIN))))
                    .groupBy(review.movieId)
                    .orderBy(review.count().desc())
                    .limit(10)
                    .fetch();

        }else{

            movieIdList = queryFactory.select(reviewGenre.review.movieId)
                    .from(reviewGenre)
                    .where(reviewGenre.genre.id.eq(genreId)
                            .and(review.spoiler.eq(false))
                            .and(review.parentReview.isNull()
                                    .and(review.createdAt.after(LocalDateTime.now().minusDays(30).with(LocalTime.MIN)))))
                    .groupBy(reviewGenre.review.movieId)
                    .orderBy(reviewGenre.review.count().desc())
                    .limit(10)
                    .fetch();
        }

        List<TopTenDTO> resultList = new ArrayList<>();

        for(Long movieId: movieIdList){

            Map<String, Object> oneByID = getOneByID(movieId);

            Double rate = queryFactory.select(review.star.avg())
                    .from(review)
                    .where(review.parentReview.isNull()
                            .and(review.movieId.eq(movieId)))
                    .fetchOne();

            Long reviewCount = queryFactory.select(review.count())
                    .from(review)
                    .where(review.parentReview.isNull()
                            .and(review.movieId.eq(movieId)))
                    .fetchOne();

            NumberTemplate<Long> likeCountSubquery = Expressions.numberTemplate(Long.class,
                    "(select count(*) from ReviewLikeEntity where review.id = {0})", review.id);

            List<TopTenReviewDTO> reviewList = queryFactory.select(Projections.constructor(
                            TopTenReviewDTO.class,
                            review.id.as("reviewId"),
                            review.star,
                            review.content,
                            likeCountSubquery.as("likeCount"),
                            review.user.profile))
                    .from(review)
                    .where(review.parentReview.isNull()
                            .and(review.spoiler.eq(false))
                            .and(review.movieId.eq(movieId)))
                    .orderBy(likeCountSubquery.desc())
                    .limit(3)
                    .fetch();

            TopTenDTO result = TopTenDTO.builder()
                    .movieId(movieId)
                    .movienm(oneByID.get("title").toString())
                    .poster_path("https://image.tmdb.org/t/p/original"+oneByID.get("poster_path").toString())
                    .release_date(oneByID.get("release_date").toString()) //문자열 잘라야됨
                    .genres((List<Map<String, Object>>) oneByID.get("genres"))
                    .TMDBRate(Double.valueOf(oneByID.get("vote_average").toString()))
                    .rate(rate)
                    .reviewCount(reviewCount)
                    .reviewList(reviewList)
                    .build();

            resultList.add(result);
        }

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }


    //TODO 전체 리뷰 갯수
    public long TotalReviewCount(){

        return reviewRepository.count();
    }

    //TODO 최신 영화 받아오기
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

    //TODO 영화 이름 으로 영화 정보 받기
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
}
