package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.CommentPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Pageable pageable);

    Page<CommentPreViewDTO> findAllByParentReviewId(Long parentReviewId, Pageable pageable);
}
