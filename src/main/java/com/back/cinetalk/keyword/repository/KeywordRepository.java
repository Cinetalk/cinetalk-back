package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long>, KeywordRepositoryCustom {

    @Query("SELECT DISTINCT k.keyword FROM KeywordEntity k " +
            "WHERE k.movieId = :movieId " +
            "GROUP BY k.keyword " +
            "ORDER BY MAX(k.updatedAt) DESC " +
            "LIMIT 4")
    List<String> findDistinctKeywordsByMovieIdOrderByCreatedAtDesc(@Param("movieId") Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}

