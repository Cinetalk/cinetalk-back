package com.back.cinetalk.find.entity;

import com.back.cinetalk.config.entity.BaseEntity;
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
public class FindEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String findword;

    private LocalDate regdate;

    public static FindEntity ToFindEntity(FindDTO findDTO){
        return FindEntity.builder()
                .id(findDTO.getId())
                .findword(findDTO.getFindword())
                .build();
    }
}
