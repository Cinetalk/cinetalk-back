package com.back.cinetalk.review.repository;

import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // JpaRepository를 상속 받으면 이 어노테이션을 지원함.
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> , QuerydslPredicateExecutor<ReviewEntity>, ReviewRepositoryCustom {

   boolean existsByUserIdAndMovieId(Long userId, Long movieId);

   List<ReviewEntity> findTop10ByContentContainingAndParentReviewIsNullOrderByCreatedAtAsc(String Content);

   List<ReviewEntity> findByUserAndParentReviewIsNullOrderByCreatedAt(UserEntity user);

   List<ReviewEntity> findByUserAndParentReviewIsNullOrderByCreatedAtDesc(UserEntity user);

   int countByParentReview(ReviewEntity reviewEntity);

   @Query("SELECT COUNT(rg) FROM ReviewGenreEntity rg JOIN rg.review r WHERE r.user = :user AND rg.genre = :genre")
   long countByUserAndGenre(@Param("user") UserEntity user, @Param("genre") GenreEntity genre);
}
