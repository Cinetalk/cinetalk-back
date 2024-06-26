package com.back.cinetalk.didnotwhatchmovie.entity;

import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "Dwm", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "movie_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DidNotWatchMovieEntity {

    @Id @GeneratedValue
    @Column(name = "dwm_id")
    private Long id;

}

