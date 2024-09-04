package com.back.cinetalk.damage.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Damage")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;
}
