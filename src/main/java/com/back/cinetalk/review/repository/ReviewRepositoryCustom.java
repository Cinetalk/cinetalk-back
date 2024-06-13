package com.back.cinetalk.review.repository;

import com.back.cinetalk.review.dto.ReReviewPreViewDTO;
import com.back.cinetalk.review.dto.ReviewPreViewDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {
    Page<ReviewPreViewDTO> findAllByMovieId(Long movieId, Pageable pageable);

    Page<ReReviewPreViewDTO> findAllByParentReviewId(Long parentReviewId, Pageable pageable);
}
