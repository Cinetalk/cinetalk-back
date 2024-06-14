package com.back.cinetalk.genre;

import com.back.cinetalk.review_genre.ReviewGenreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "genre")
    private List<ReviewGenreEntity> reviewGenreEntityList;

    @OneToMany(mappedBy = "genre")
    private List<BadgeEntity> badgeEntityList = new ArrayList<BadgeEntity>();
}
