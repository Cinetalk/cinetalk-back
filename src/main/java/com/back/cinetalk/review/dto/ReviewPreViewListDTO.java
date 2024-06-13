package com.back.cinetalk.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPreViewListDTO {

    List<ReviewPreViewDTO> reviewList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;

    public static ReviewPreViewListDTO toReviewPreViewListDTO(Page<ReviewPreViewDTO> reviewList) {
        return ReviewPreViewListDTO.builder()
                .isLast(reviewList.isLast())
                .isFirst(reviewList.isFirst())
                .totalPage(reviewList.getTotalPages())
                .totalElements(reviewList.getTotalElements())
                .listSize(reviewList.getNumberOfElements())
                .reviewList(reviewList.getContent())
                .build();
    }
}
