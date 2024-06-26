package com.back.cinetalk.user.MyPage.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkByUserResponseDTO {

    private Long id;
    private Long movie_id;
    private String poster_path;
}
