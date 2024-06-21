package com.back.cinetalk.user.MyPage.dto.activity;

import com.back.cinetalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeByUserResponseDTO {

    private String badge_name;
    private String genre_name;
    private char useyn;
}
