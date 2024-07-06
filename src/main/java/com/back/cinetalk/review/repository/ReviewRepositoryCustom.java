package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.CommentPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

    Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Pageable pageable);

    Page<CommentPreViewDTO> findAllByParentReviewId(Long parentReviewId, Pageable pageable);

    List<ReviewPreViewDTO> findBestReviews(Long movieId, int limit);

    Page<ReviewPreViewDTO> findGeneralReviewsExcludingBest(Long movieId, List<Long> bestReviewIds, Pageable pageable);
}
