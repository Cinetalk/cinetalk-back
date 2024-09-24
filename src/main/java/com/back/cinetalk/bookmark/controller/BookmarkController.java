package com.back.cinetalk.bookmark.controller;

import com.back.cinetalk.bookmark.service.BookmarkService;
import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.user.jwt.JwtValidation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{movieId}")
    @Operation(summary = "영화 찜 API", description = "특정 영화를 찜하는 API 입니다.")
    public StateRes bookmarkMovie(@PathVariable(name = "movieId") Long movieId,
                                  @JwtValidation String email) {

        return bookmarkService.bookmarkMovie(movieId, email);
    }

    @GetMapping("/{movieId}/check")
    @Operation(summary = "영화 찜 상태를 알려주는 API", description = "특정 영화의 찜 상태를 알려주는 API 입니다.")
    public StateRes bookmarkCheck(@PathVariable(name = "movieId") Long movieId,
                                  @JwtValidation String email) {

        return bookmarkService.bookmarkCheck(movieId, email);
    }
}
