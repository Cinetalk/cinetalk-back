package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {

    List<KeywordEntity> findAllByMovieIdOrderByCountDesc(String movieId);

    KeywordEntity findByKeywordAndMovieId(String keyword, String movieId);

}
