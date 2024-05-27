package com.back.cinetalk.movie.controller;

import com.back.cinetalk.movie.dto.MovieDetailDTO;
import com.back.cinetalk.movie.service.MovieService;
import com.back.cinetalk.review.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/list")
    @Operation(summary = "영화 리스트",description = "최신 영화 리스트")
    @ApiResponse(responseCode = "200",description = "출력완료",
            content = @Content(schema = @Schema(implementation = ResponseBody.class
            )))
    public List<Map<String, Object>> list() throws IOException {

        return movieService.nowPlayingList();
    }

    @PostMapping("/test")
    public Map<String, Object> getSearchlist(String query) throws IOException {

        return movieService.getOneByName(query);
    }

    @GetMapping("/{movie_id}")
    public MovieDetailDTO getMovieDetails(@PathVariable String movie_id) throws IOException {
        return movieService.getMovieDetail(movie_id);
    }

    @PostMapping("/imagetest")
    public String imagetest(@RequestParam(value = "url") String url)throws  Exception{

        // 이미지 다운로드
        BufferedImage image = ImageIO.read(new URL(url));

        // 이미지의 폭과 높이
        int width = image.getWidth();
        int height = image.getHeight();

        // 모든 픽셀의 RGB 값을 합산
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        long totalPixels = width * height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                totalRed += (rgb >> 16) & 0xFF;
                totalGreen += (rgb >> 8) & 0xFF;
                totalBlue += rgb & 0xFF;
            }
        }

        // RGB 값의 평균 계산
        int avgRed = (int) (totalRed / totalPixels);
        int avgGreen = (int) (totalGreen / totalPixels);
        int avgBlue = (int) (totalBlue / totalPixels);

        // 평균 RGB 값을 Hex 코드로 반환
        return String.format("#%02x%02x%02x", avgRed, avgGreen, avgBlue);
    }
}
