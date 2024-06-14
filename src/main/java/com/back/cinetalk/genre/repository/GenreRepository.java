package com.back.cinetalk.genre.repository;

import com.back.cinetalk.genre.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}
