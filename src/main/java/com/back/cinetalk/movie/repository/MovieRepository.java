package com.back.cinetalk.movie.repository;

import com.back.cinetalk.movie.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findByCreatedAt(LocalDateTime createdAt);

    MovieEntity findFirstByOrderByCreatedAtAsc();

    @Query("SELECT m FROM MovieEntity m JOIN m.genres g WHERE g.genre.id IN :genreIds")
    List<MovieEntity> findByGenreIds(List<Long> genreIds);

}
