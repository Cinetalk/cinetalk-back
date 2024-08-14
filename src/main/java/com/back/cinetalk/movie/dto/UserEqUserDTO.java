package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEqUserDTO {

    private Long userId;
    private String nickname;
    private byte[] profile;
}
