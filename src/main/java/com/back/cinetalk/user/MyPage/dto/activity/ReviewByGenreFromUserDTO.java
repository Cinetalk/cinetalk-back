package com.back.cinetalk.user.MyPage.dto.activity;

import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class ReviewByGenreFromUserDTO {

    private Long id;
    private String name;
    private Long count;

    public ReviewByGenreFromUserDTO(Long id,String name,Long count){
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
