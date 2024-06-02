package com.back.cinetalk.find.dto;

import com.back.cinetalk.find.entity.FindEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "검색 키워드 담기는 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindDTO {

    private Long id;

    private String keword;

    private LocalDate regdate;

    public static FindDTO ToFindDTO(FindEntity findEntity){
        return FindDTO.builder()
                .id(findEntity.getId())
                .keword(findEntity.getKeword())
                .regdate(findEntity.getRegdate())
                .build();
    }
}
