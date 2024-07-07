package com.back.cinetalk.find.service;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.find.dto.FindDTO;
import com.back.cinetalk.find.dto.FindReviewDTO;
import com.back.cinetalk.find.entity.FindEntity;
import com.back.cinetalk.find.entity.QFindEntity;
import com.back.cinetalk.find.repository.FindRepository;
import com.back.cinetalk.movie.service.CallAPI;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.dto.UserDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindService {

    private final FindRepository findRepository;
    private final CallAPI callAPI;
    private final JPAQueryFactory queryFactory;
    private final AdultContentFinder adultContentFinder;


    //TODO 검색어 저장
    public StateRes WordSave(String findword){

        FindDTO findDTO = new FindDTO();

        findDTO.setFindword(findword);

        FindEntity findEntity = FindEntity.ToFindEntity(findDTO);

        findRepository.save(findEntity);

        return new StateRes(true);
    }

    //TODO 연관 검색어 출력
    public List<String> findText(String query) throws IOException {

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query="+query;

        Map<String, Object> list = callAPI.callAPI(url);

        List<Map<String,Object>> resultlist = (List<Map<String, Object>>) list.get("results");

        return resultlist.stream()
                .map(result -> (String) result.get("title"))
                .collect(Collectors.toList());
    }

    //TODO 영화 검색 결과
    public List<Map<String,Object>> MovieResult(String query)throws Exception{

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query="+query;

        Map<String, Object> list = callAPI.callAPI(url);

        List<Map<String,Object>> resultApi = (List<Map<String, Object>>) list.get("results");

        List<Integer> movieIdList = resultApi.stream()
                .map(result -> (Integer) result.get("id"))
                .toList();

        List<Map<String,Object>> returnList = new ArrayList<>();

        for (int movieId : movieIdList) {

            String value = adultContentFinder.adultFinder((long) movieId);

            if(value.equals("pass")){

                System.out.println("들어온 영화값"+movieId);

                String detailUrl = "https://api.themoviedb.org/3/movie/"+movieId+"?language=ko";

                Map<String, Object> movie = callAPI.callAPI(detailUrl);

                returnList.add(movie);
            }
        }

        return returnList;
    }

    //TODO 리뷰 검색 결과
    public List<FindReviewDTO> ReviewResult(String query) {

        QReviewEntity review = QReviewEntity.reviewEntity;

        BooleanExpression predicate = review.content.like("%" + query + "%");

        List<ReviewEntity> result = queryFactory.selectFrom(review)
                .where(predicate)
                .orderBy(review.createdAt.asc())
                .fetch();

        List<FindReviewDTO> returnList = new ArrayList<>();

        for (ReviewEntity reviewEntity : result) {

            FindReviewDTO findReviewDTO = new FindReviewDTO();

            ReviewDTO dto = ReviewDTO.toReviewDTO(reviewEntity);

            findReviewDTO.setReviewDTO(dto);

            findReviewDTO.setUserDTO(UserDTO.ToUserDTO(reviewEntity.getUser()));

            returnList.add(findReviewDTO);
        }

        return returnList;
    }

    //TODO 인기 검색어
    public ResponseEntity<?> PopularFind(){

        QFindEntity find = QFindEntity.findEntity;

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7).with(LocalTime.MIDNIGHT);

        List<String> list = queryFactory.select(find.findword).from(find)
                .where(find.createdAt.gt(sevenDaysAgo))
                .groupBy(find.findword)
                .orderBy(find.count().desc(), find.createdAt.asc(), find.findword.asc())
                .limit(10)
                .fetch();

        return new ResponseEntity<>(list,HttpStatus.OK);
    }

}
