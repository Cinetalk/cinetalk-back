package com.back.cinetalk.reviewGenre.repository;

import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewGenreRepository extends JpaRepository<ReviewGenreEntity, Long> {

    List<ReviewGenreEntity> findByReview(ReviewEntity review);
}
