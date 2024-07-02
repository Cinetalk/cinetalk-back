package com.back.cinetalk.genre.entity;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Genre")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "genre",cascade = CascadeType.ALL)
    private List<ReviewGenreEntity> reviewGenreEntityList = new ArrayList<ReviewGenreEntity>();

    @OneToMany(mappedBy = "genre",cascade = CascadeType.ALL)
    private List<BadgeEntity> badgeEntityList = new ArrayList<BadgeEntity>();
}
