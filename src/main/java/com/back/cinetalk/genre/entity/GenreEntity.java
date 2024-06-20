package com.back.cinetalk.genre.entity;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.review_genre.entity.ReviewGenreEntity;
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

    private String badgename;

    @OneToMany(mappedBy = "genre")
    private List<ReviewGenreEntity> reviewGenreEntityList;

    @OneToMany(mappedBy = "genre")
    private List<BadgeEntity> badgeEntityList = new ArrayList<BadgeEntity>();
}
