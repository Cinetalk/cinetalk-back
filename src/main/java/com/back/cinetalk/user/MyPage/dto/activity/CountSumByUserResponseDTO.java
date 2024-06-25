package com.back.cinetalk.user.MyPage.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CountSumByUserResponseDTO {

    private Long rateCount;
    private Long reviewCount;
    private Long bookmarkCount;
}
