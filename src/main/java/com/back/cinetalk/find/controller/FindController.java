package com.back.cinetalk.find.controller;

import com.back.cinetalk.find.service.FindService;
import com.back.cinetalk.movie.service.MovieDetailService;
import com.back.cinetalk.review.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/find")
@RequiredArgsConstructor
public class FindController {

    private final FindService findService;
    private final MovieDetailService movieDetailService;

    @PostMapping("/findSave")
    @Operation(summary = "검색어 저장",description = "인기 검색어 순위를 위한 검색어 저장 프로세스")
    @ApiResponse(responseCode = "200",description = "저장완료",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> WordSave(@RequestParam(value = "keword") String keword){

        return findService.WordSave(keword);
    }

    @GetMapping("/findText")
    @Operation(summary = "연관 검색어 표출",description = "검색할 때마다 연관 검색어를 불러오는 프로세스")
    @ApiResponse(responseCode = "200",description = "연관 검색어 출력",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> findText(@RequestParam(value = "query")String query) throws IOException {

        List<String> result = findService.findText(query);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/findResult")
    @Operation(summary = "검색 결과",description = "검색어를 기반으로 영화,리뷰를 불러오는 프로세스")
    @ApiResponse(responseCode = "200",description = "결과 출력",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> findResult(@RequestParam(value = "query")String query) throws Exception {

        //반환 map 생성
        Map<String,Object> result = new HashMap<>();

        //영화 결과 정렬
        List<Map<String, Object>> movielist = findService.MovieResult(query);
        if (movielist.size() > 6) {
            movielist = movielist.subList(0, 6);
        }
        result.put("movielist",movielist);


        //리뷰 결과 정렬
        List<ReviewDTO> reviewlist = findService.ReviewResult(query);
        if (reviewlist.size() > 16) {
            reviewlist = reviewlist.subList(0, 16);
        }
        result.put("reviewlist",reviewlist);


        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/findMovie")
    @Operation(summary = "(검색)영화 조회",description = "검색어에 따른 모든 영화 조회 프로세스")
    public ResponseEntity<?> findMovie(@RequestParam(value = "query")String query) throws Exception {

        List<Map<String, Object>> movielist = findService.MovieResult(query);

        return new ResponseEntity<>(movielist,HttpStatus.OK);
    }

    @GetMapping("/findReview")
    @Operation(summary = "(검색)리뷰 조회",description = "검색어에 따른 모든 리뷰 조회 프로세스")
    public ResponseEntity<?> findReview(@RequestParam(value = "query")String query){

        List<ReviewDTO> reviewlist = findService.ReviewResult(query);

        return new ResponseEntity<>(reviewlist,HttpStatus.OK);
    }
}
