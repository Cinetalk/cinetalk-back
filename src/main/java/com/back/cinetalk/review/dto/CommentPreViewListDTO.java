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
public class CommentPreViewListDTO {

    List<CommentPreViewDTO> reReviewList;
    Integer listSize;
    Integer totalPage;
    Long totalElements;
    Boolean isFirst;
    Boolean isLast;

    public static CommentPreViewListDTO toReReviewPreViewListDTO(Page<CommentPreViewDTO> commentPreViewDTOList) {
        return CommentPreViewListDTO.builder()
                .isLast(commentPreViewDTOList.isLast())
                .isFirst(commentPreViewDTOList.isFirst())
                .totalPage(commentPreViewDTOList.getTotalPages())
                .totalElements(commentPreViewDTOList.getTotalElements())
                .listSize(commentPreViewDTOList.getNumberOfElements())
                .reReviewList(commentPreViewDTOList.getContent())
                .build();
    }
}
