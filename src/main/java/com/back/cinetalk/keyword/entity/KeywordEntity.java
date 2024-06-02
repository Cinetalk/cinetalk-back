package com.back.cinetalk.keyword.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@Table(name = "Keyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String movieId;

    private String keyword;

    private int count;

    public void updateCount(int count) {
        this.count += count;
    }
}
