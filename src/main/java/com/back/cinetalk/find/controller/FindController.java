package com.back.cinetalk.find.controller;

import com.back.cinetalk.find.service.FindService;
import com.back.cinetalk.movie.service.MovieService;
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
    private final MovieService movieService;

    @PostMapping("/findSave")
    @Operation(summary = "검색어 저장",description = "인기 검색어 순위를 위한 검색어 저장 프로세스")
    @ApiResponse(responseCode = "200",description = "저장완료",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> WordSave(@RequestParam(value = "keword") String keword){

        return findService.WordSave(keword);
    }

    @PostMapping("/findText")
    @Operation(summary = "연관 검색어 표출",description = "검색할 때마다 연관 검색어를 불러오는 프로세스")
    @ApiResponse(responseCode = "200",description = "연관 검색어 출력",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> findText(@RequestParam(value = "query")String query) throws IOException {

        List<String> result = findService.findText(query);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/findResult")
    @Operation(summary = "검색 결과",description = "검색어를 기반으로 영화,리뷰를 불러오는 프로세스")
    @ApiResponse(responseCode = "200",description = "결과 출력",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    public ResponseEntity<?> findResult(@RequestParam(value = "query")String query) throws Exception {

        Map<String,Object> result = new HashMap<>();


        List<Map<String, Object>> movielist = findService.MovieResult(query);
        result.put("movielist",movielist);



        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/findReview")
    @Operation(summary = "(검색)리뷰 조회",description = "검색어에 따른 리뷰 조회 프로세스")
    public ResponseEntity<?> ReviewSearch(@RequestParam(value = "query")String query){


        return new ResponseEntity<>("succcess",HttpStatus.OK);
    }
}
