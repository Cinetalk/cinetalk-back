package com.back.cinetalk.genre.entity;

import com.back.cinetalk.didnotwhatchmovie.entity.MovieGenreEntity;
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

    // 이 영화 보셨나요? (MovieGenreEntity로 연결)
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenreEntity> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "genre")
    private List<ReviewGenreEntity> reviewGenreEntityList = new ArrayList<ReviewGenreEntity>();

    @OneToMany(mappedBy = "genre")
    private List<BadgeEntity> badgeEntityList = new ArrayList<BadgeEntity>();
}
