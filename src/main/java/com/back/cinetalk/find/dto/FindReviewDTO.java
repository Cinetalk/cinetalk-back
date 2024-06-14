package com.back.cinetalk.find.dto;

import com.back.cinetalk.review.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindReviewDTO {
    private ReviewDTO reviewDTO;
    private Long userId;
}
