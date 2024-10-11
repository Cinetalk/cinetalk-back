package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long>, KeywordRepositoryCustom {

    @Query("SELECT k FROM KeywordEntity k " +
            "WHERE k.movieId = :movieId " +
            "AND k.updatedAt = (SELECT MAX(k2.updatedAt) FROM KeywordEntity k2 WHERE k2.keyword = k.keyword) " +
            "ORDER BY k.updatedAt DESC " +
            "LIMIT 4")
    List<KeywordEntity> findDistinctKeywordsByMovieIdOrderByUpdatedAtDesc(@Param("movieId") Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    Optional<KeywordEntity> findByMovieIdAndUser(Long movieId, UserEntity user);

    Long countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
}

