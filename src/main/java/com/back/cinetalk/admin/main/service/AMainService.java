package com.back.cinetalk.admin.main.service;

import com.back.cinetalk.admin.main.dto.MainResponseDTO;
import com.back.cinetalk.keyword.repository.KeywordRepository;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AMainService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final KeywordRepository keywordRepository;

    private LocalDateTime startOfMonth(int minusMonths) {
        return LocalDate.now()
                .minusMonths(minusMonths)
                .withDayOfMonth(1)
                .atStartOfDay();
    }

    private List<MainResponseDTO> getCountList(CountFetcher countFetcher) {
        List<MainResponseDTO> resultList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LocalDateTime fromDate = startOfMonth(i);
            LocalDateTime toDate = startOfMonth(i - 1);

            String yearMonth = fromDate.toLocalDate().toString().substring(0, 7);
            Long count = countFetcher.fetch(fromDate, toDate);

            MainResponseDTO result = MainResponseDTO.builder()
                    .date(yearMonth)
                    .count(count)
                    .build();

            resultList.add(result);
        }
        return resultList;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<MainResponseDTO>> userCountList() {
        List<MainResponseDTO> resultList = getCountList(userRepository::countByCreatedAtBetween);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<MainResponseDTO>> reviewCountList() {
        List<MainResponseDTO> resultList = getCountList((fromDate, toDate) ->
                reviewRepository.countByParentReviewIsNullAndCreatedAtBetween(fromDate, toDate));
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<MainResponseDTO>> keywordCountList() {
        List<MainResponseDTO> resultList = getCountList(keywordRepository::countByCreatedAtBetween);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @FunctionalInterface
    private interface CountFetcher {
        Long fetch(LocalDateTime fromDate, LocalDateTime toDate);
    }
}
