package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

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

    public static ReviewPreViewListDTO toReviewPreViewListDTO(Page<ReviewEntity> reviewList) {

        List<ReviewPreViewDTO> reviewPreViewDTOList = reviewList.stream()
                .map(ReviewPreViewDTO::toReviewPreViewDTO).collect(Collectors.toList());

        return ReviewPreViewListDTO.builder()
                .isLast(reviewList.isLast())
                .isFirst(reviewList.isFirst())
                .totalPage(reviewList.getTotalPages())
                .totalElements(reviewList.getTotalElements())
                .listSize(reviewPreViewDTOList.size())
                .reviewList(reviewPreViewDTOList)
                .build();
    }
}
