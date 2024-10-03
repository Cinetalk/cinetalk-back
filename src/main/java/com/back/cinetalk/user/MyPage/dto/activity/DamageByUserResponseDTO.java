package com.back.cinetalk.user.MyPage.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DamageByUserResponseDTO {

    private String movienm;
    private String review_content;
    private String category;
    private String startDate;
    private String endDate;
    private Long date;
}
