package com.back.cinetalk.movie.controller;

import com.back.cinetalk.movie.dto.BannerDTO;
import com.back.cinetalk.movie.dto.MentionDTO;
import com.back.cinetalk.movie.dto.MovieDetailDTO;
import com.back.cinetalk.movie.dto.UserEqDTO;
import com.back.cinetalk.movie.service.MainColorExtract;
import com.back.cinetalk.movie.service.MovieMainService;
import com.back.cinetalk.movie.service.MovieDetailService;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieDetailService movieDetailService;
    private final MovieMainService movieMainService;
    private final MainColorExtract mainColorExtract;
    private final UserByAccess userByAccess;

    @GetMapping("/{movieId}")
    @Operation(summary = "영화 상세정보 ",description = "영화 상세정보 api")
    public MovieDetailDTO getMovieDetails(@PathVariable Long movieId) throws IOException {
        return movieDetailService.getMovieDetail(movieId);
    }


    @GetMapping("/HidingPiece")
    @Operation(summary = "숨겨진 명작",description = "리뷰가 5~20개 인 영화중 평점이 4점 이상인 랜덤한 영화 10개 표출")
    public ResponseEntity<?> HidingPiece() throws IOException {
        
        return movieMainService.HidingPiece();
    }

    @GetMapping("/MentionKeword")
    @Operation(summary = "자주 언급되는 키워드",description = "오늘 자 모든 리뷰의 자주 언급된 키워드 상위 5개가 들어간 리뷰 10개씩을 출력")
    @ApiResponse(responseCode = "200",description = "출력완료",
            content = @Content(schema = @Schema(implementation = MentionDTO.class)))
    public ResponseEntity<?> MentionKeyword(){

        long startTime = System.currentTimeMillis();

        ResponseEntity<?> list = movieMainService.MentionKeyword();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("MentionKeyword method execution time: " + duration + " milliseconds");

        return list;
    }

    @GetMapping("/TotalReviewCount")
    @Operation(summary = "톡이 쌓였어요",description = "등록된 모든 리뷰의 개수 출력")
    @ApiResponse(responseCode = "200",description = "출력완료",
            content = @Content(schema = @Schema(implementation = Integer.class)))
    public ResponseEntity<?> TotalReviewCount(){

        return new ResponseEntity<>(movieMainService.TotalReviewCount(),HttpStatus.OK);
    }

    @GetMapping("/imagecolor")
    @Operation(summary = "상세페이지용 이미지 주요색 출력",description = "이미지의 url 입력시 이미지의 주요색 hex코드 반환 api(현재 사용안함)")
    public String imageColor(@RequestParam(value = "url") String url)throws  Exception{

        return mainColorExtract.ColorExtract(url);
    }

    @GetMapping("/HoxyWatching")
    @Operation(summary = "혹시 이 영화 보셨나요?"
            ,description = "사용자가 평가하지 않는 영화들 중 평가가 많은 영화 리스트 출력,사용자가 평가를 했다면 많이 평가한 장르의 영화를 보여줌,비로그인 시 평가가 많은 영화보여줌")
    public ResponseEntity<?> HoxyWatching(HttpServletRequest request) throws IOException {

        return movieMainService.HoxyWatching(request);
    }

    @GetMapping("/top-reviewers")
    @Operation(summary = "나와 취향이 같은 사람들",description = "리뷰를 가장 많이 작성한 유저를 가져옴")
    @ApiResponse(responseCode = "200",description = "출력완료", content = @Content(schema = @Schema(implementation = UserEqDTO.class)))
    public ResponseEntity<?> getTopReviewers(HttpServletRequest request) throws IOException {

        return movieMainService.UserEqReviewers(request);
    }

    @GetMapping("/MainBanner")
    @Operation(summary = "메인 페이지 배너",description = "최근 일주일 동안 리뷰가 가장 많이 달린 영화 TOP 3 및 댓글 표시")
    @ApiResponse(responseCode = "200",description = "출력완료",
            content = @Content(schema = @Schema(implementation = BannerDTO.class)))
    public ResponseEntity<?> MainBanner() throws IOException {

        return movieMainService.mainBanner();
    }

    @GetMapping("/TopTenTalk")
    @Operation(summary = "영화톡 Top10",description = "전체 또는 선택한 장르의 30일 까지의 리뷰많은 영화 10개,영화당 리뷰 3개 표시")
    public ResponseEntity<?> TopTenTalk(@RequestParam(value = "genreId") Long genreId) throws IOException {

        return movieMainService.TopTenTalk(genreId);
    }

    @GetMapping("/TotalMovieId")
    @Operation(summary = "영화Id모음",description = "댓글 또는 키워드가 입력된 모든 영화의 id 반환")
    public ResponseEntity<?> TotalMovieId(){

        return movieMainService.TotalMovieId();
    }
}
