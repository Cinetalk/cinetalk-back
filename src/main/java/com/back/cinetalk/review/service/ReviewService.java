package com.back.cinetalk.review.service;

import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void reviewSave(ReviewDTO reviewDTO){

        ReviewEntity reviewEntity= ReviewEntity.ToReviewEntity(reviewDTO);

        reviewRepository.save(reviewEntity);
    }


}
