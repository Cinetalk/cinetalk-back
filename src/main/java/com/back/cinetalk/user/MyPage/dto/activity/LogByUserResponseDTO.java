package com.back.cinetalk.user.MyPage.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogByUserResponseDTO {

    private String category;
    private Long id;
    private Long movieId;
    private String movienm;
    private String poster_path;
    private double star;
    private String content;
    private LocalDateTime createdAt;
}
