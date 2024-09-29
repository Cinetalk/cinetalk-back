package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.CommentPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

    Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Long userId, Pageable pageable, String sortType);

    Page<CommentPreViewDTO> findAllByParentReviewId(Long parentReviewId, Long userId, Pageable pageable);

    List<ReviewPreViewDTO> findBestReviews(Long movieId, Long userId, int limit);

    Page<ReviewPreViewDTO> findGeneralReviewsExcludingBest(Long movieId, Long userId, List<Long> bestReviewIds, Pageable pageable, String sortType);
}
