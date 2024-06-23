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

    private Long genre_id;
    private String genre_name;
    private String badge_name;
    private boolean isUse;
}
