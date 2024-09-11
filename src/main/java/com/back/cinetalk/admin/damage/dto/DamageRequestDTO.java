package com.back.cinetalk.admin.damage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DamageRequestDTO {

    private int report_id;

    private int date;
}
