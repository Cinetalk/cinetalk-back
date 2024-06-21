package com.back.cinetalk.didnotwhatchmovie.entity;

import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.movie.entity.MovieEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Movie_Genre")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieGenreEntity {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private MovieEntity Movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;
}
