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
public class ReReviewPreViewListDTO {

    List<ReReviewPreViewDTO> reReviewList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;

    public static ReReviewPreViewListDTO toReReviewPreViewListDTO(Page<ReReviewPreViewDTO> reReviewList) {
        return ReReviewPreViewListDTO.builder()
                .isLast(reReviewList.isLast())
                .isFirst(reReviewList.isFirst())
                .totalPage(reReviewList.getTotalPages())
                .totalElements(reReviewList.getTotalElements())
                .listSize(reReviewList.getNumberOfElements())
                .reReviewList(reReviewList.getContent())
                .build();
    }
}
