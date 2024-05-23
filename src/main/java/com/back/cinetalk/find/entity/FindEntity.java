package com.back.cinetalk.find.entity;

import com.back.cinetalk.find.dto.FindDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Find")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keword;

    private LocalDate regdate;

    @PrePersist
    public void prePersist() {
        this.regdate = LocalDate.now(); // 현재 날짜를 설정
    }

    public static FindEntity ToFindEntity(FindDTO findDTO){
        return FindEntity.builder()
                .id(findDTO.getId())
                .keword(findDTO.getKeword())
                .regdate(findDTO.getRegdate())
                .build();
    }
}
