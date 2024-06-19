package com.back.cinetalk.find.dto;

import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.user.dto.UserDTO;
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
    private UserDTO userDTO;
}
