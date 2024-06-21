package com.back.cinetalk.user.MyPage.controller;

import com.back.cinetalk.user.MyPage.dto.activity.BadgeByUserResponseDTO;
import com.back.cinetalk.user.MyPage.dto.bookmark.BookmarkByUserResponseDTO;
import com.back.cinetalk.user.MyPage.service.MyPage_BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPage_BookmarkController {

    private final MyPage_BookmarkService myPageBookmarkService;

    @GetMapping("/BookmarkByUser")
    @Operation(summary = "유저의 찜 목록",description = "토큰과 같이 요청시 유저가 찜한 영화의 목록 리스트 출력")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = BookmarkByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> BookmarkByUser(HttpServletRequest request) throws IOException {return myPageBookmarkService.BookmarkByUser(request);}

}
