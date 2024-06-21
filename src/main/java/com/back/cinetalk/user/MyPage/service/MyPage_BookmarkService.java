package com.back.cinetalk.user.MyPage.service;

import com.back.cinetalk.bookmark.entity.BookmarkEntity;
import com.back.cinetalk.bookmark.repository.BookmarkRepository;
import com.back.cinetalk.movie.service.MovieMainService;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.MyPage.dto.bookmark.BookmarkByUserResponseDTO;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPage_BookmarkService {

    private final UserByAccess userByAccess;
    private final BookmarkRepository bookmarkRepository;
    private final MovieMainService movieMainService;

    public ResponseEntity<?> BookmarkByUser(HttpServletRequest request) throws IOException {

        UserEntity userEntity = userByAccess.getUserEntity(request);

        List<BookmarkEntity> list = bookmarkRepository.findByUserOrderByCreatedAt(userEntity);

        List<BookmarkByUserResponseDTO> result = new ArrayList<>();

        for (BookmarkEntity bookmarkEntity:list) {

            Map<String, Object> oneByID = movieMainService.getOneByID(bookmarkEntity.getMovieId());

            BookmarkByUserResponseDTO responseDTO = BookmarkByUserResponseDTO.builder()
                    .id(bookmarkEntity.getId())
                    .movie_id(bookmarkEntity.getMovieId())
                    .poster_path("https://image.tmdb.org/t/p/original"+oneByID.get("poster_path"))
                    .build();

            result.add(responseDTO);
        }

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
