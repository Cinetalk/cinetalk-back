package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEqDTO {

    private Long userId;
    private String nickname;
    private byte[] profile;
    private Long reviewCount;
    private Long rateCount;
    private List<String> badges;

}
