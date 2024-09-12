package com.back.cinetalk.admin.damage.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamageRequestDTO {

    private String category;

    private int date;
}
