package com.back.cinetalk.bookmark.service;


import com.back.cinetalk.bookmark.entity.BookmarkEntity;
import com.back.cinetalk.bookmark.repository.BookmarkRepository;
import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @Transactional
    public StateRes bookmarkMovie(Long movieId, String email) {
        UserEntity user = userRepository.findByEmail(email);

        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .user(user)
                .movieId(movieId)
                .build();

        bookmarkRepository.save(bookmarkEntity);
        return new StateRes(true);
    }
}

