package com.back.cinetalk.movie.controller;

import com.back.cinetalk.movie.dto.MovieDetailDTO;
import com.back.cinetalk.movie.service.MainColorExtract;
import com.back.cinetalk.movie.service.MovieMainService;
import com.back.cinetalk.movie.service.MovieDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieDetailService movieDetailService;
    private final MovieMainService movieMainService;
    private final MainColorExtract mainColorExtract;

    @GetMapping("/list")
    @Operation(summary = "영화 리스트",description = "최신 영화 리스트")
    @ApiResponse(responseCode = "200",description = "출력완료",
            content = @Content(schema = @Schema(implementation = ResponseBody.class
            )))
    public List<Map<String, Object>> list() throws IOException {

        return movieMainService.nowPlayingList();
    }

    @PostMapping("/test")
    public Map<String, Object> getSearchlist(String query) throws IOException {

        return movieMainService.getOneByName(query);
    }

    @GetMapping("/{movie_id}")
    public MovieDetailDTO getMovieDetails(@PathVariable String movie_id) throws IOException {
        return movieDetailService.getMovieDetail(movie_id);
    }

    @GetMapping("/ReviewByUser")
    public ResponseEntity<?> ReviewByUser(HttpServletRequest request) throws IOException {

        List<Map<String, Object>> maps = movieMainService.ReviewByUser(request);

        return new ResponseEntity<>(maps, HttpStatus.OK);
    }

    @GetMapping("/HidingPiece")
    public ResponseEntity<?> HidingPiece() throws IOException {

        List<Map<String, Object>> maps = movieMainService.HidingPiece();

        return new ResponseEntity<>(maps, HttpStatus.OK);
    }

    @GetMapping("/MentionKeword")
    public ResponseEntity<?> MentionKeword(){

        movieMainService.MentionKeword();

        return new ResponseEntity<>("",HttpStatus.OK);
    }

    @GetMapping("/imagecolor")
    public String imagecolor(@RequestParam(value = "url") String url)throws  Exception{

        return mainColorExtract.ColorExtract(url);
    }
}
