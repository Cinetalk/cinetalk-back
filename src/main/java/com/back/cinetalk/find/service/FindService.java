package com.back.cinetalk.find.service;

import com.back.cinetalk.find.dto.FindDTO;
import com.back.cinetalk.find.entity.FindEntity;
import com.back.cinetalk.find.repository.FindRepository;
import com.back.cinetalk.movie.service.MovieService;
import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.QReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindService {

    private final FindRepository findRepository;
    private final MovieService movieService;
    private final JPAQueryFactory queryFactory;


    public ResponseEntity<?> WordSave(String keword){

        FindDTO findDTO = new FindDTO();

        findDTO.setKeword(keword);

        FindEntity findEntity = FindEntity.ToFindEntity(findDTO);

        findRepository.save(findEntity);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public List<String> findText(String query) throws IOException {

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query="+query;

        Map<String, Object> list = movieService.CallAPI(url);

        List<Map<String,Object>> resultlist = (List<Map<String, Object>>) list.get("results");

        List<String> returnList = new ArrayList<>();

        for (Map<String,Object> result:resultlist) {

            returnList.add((String) result.get("title"));
        }

        return returnList;
    }

    public List<Map<String,Object>> MovieResult(String query)throws Exception{

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query="+query;

        Map<String, Object> list = movieService.CallAPI(url);

        List<Map<String,Object>> resultlist = (List<Map<String, Object>>) list.get("results");

        return resultlist;
    }

    public List<ReviewDTO> ReviewResult(String query) {

        QReviewEntity review = QReviewEntity.reviewEntity;

        BooleanExpression predicate = review.content.like("%" + query + "%");

        List<ReviewEntity>  result = queryFactory.selectFrom(review)
                .where(predicate)
                .orderBy(review.regdate.asc())
                .fetch();

        List<ReviewDTO> returnList = new ArrayList<>();

        for (ReviewEntity reviewEntity : result) {

            ReviewDTO dto = ReviewDTO.ToReviewDTO(reviewEntity);

            returnList.add(dto);
        }

        return returnList;
    }
}
