package com.back.cinetalk.didnotwhatchmovie.repository;

import com.back.cinetalk.didnotwhatchmovie.entity.DidNotWhatchMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface DidNotWhatchMovieRepository extends JpaRepository<DidNotWhatchMovieEntity, Long>, QuerydslPredicateExecutor<DidNotWhatchMovieEntity> {
    Optional<DidNotWhatchMovieEntity> findByUserIdAndMovieId(Long userId, Long movieId);
    List<DidNotWhatchMovieEntity> findByUserId(Long userId);
}
