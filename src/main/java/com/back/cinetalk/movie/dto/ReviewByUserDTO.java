package com.back.cinetalk.movie.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class ReviewByUserDTO {

    private Long id;
    private String content;
    private int movie_id;
    private double star;
    private LocalDateTime regdate;
    private Long reReviewCount;
    private Long likeCount;
    private String movieposter;

    @QueryProjection
    public ReviewByUserDTO(Long id,String content,int movie_id,double star,LocalDateTime regdate,Long reReviewCount,Long likeCount,String movieposter){

        this.id = id;
        this.content = content;
        this.movie_id = movie_id;
        this.star = star;
        this.regdate = regdate;
        this.reReviewCount = reReviewCount;
        this.likeCount = likeCount;
    }
}
