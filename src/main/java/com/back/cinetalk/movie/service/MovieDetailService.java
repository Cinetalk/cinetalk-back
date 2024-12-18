package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.*;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieDetailService {

    private final CallAPI callAPI;
    private final ReviewRepository reviewRepository;

    // 영화 상세 정보
    public MovieDetailDTO getMovieDetail(Long movieId) throws IOException {

        String url1 = "https://api.themoviedb.org/3/movie/" + movieId + "?append_to_response=images,credits,release_dates&language=ko";
        Map<String, Object> stringObjectMap1 = callAPI.callAPI(url1);

        List<GenreDTO> genreList = extractGenres((List<Map<String, Object>>) stringObjectMap1.get("genres"));
        List<CastDTO> castList = extractCast((List<Map<String, Object>>) ((Map<String, Object>) stringObjectMap1.get("credits")).get("cast"));
        List<CrewDTO> crewList = extractCrew((List<Map<String, Object>>) ((Map<String, Object>) stringObjectMap1.get("credits")).get("crew"));
        String contentRating = extractContentRating(stringObjectMap1);


        String url2 = "https://api.themoviedb.org/3/movie/" + movieId + "?append_to_response=images";
        Map<String, Object> stringObjectMap2 = callAPI.callAPI(url2);
        List<ImageDTO> imageList = extractImages((List<Map<String, Object>>) ((Map<String, Object>) stringObjectMap2.get("images")).get("backdrops"));

        String language = "ko";
        String url3 = "https://api.themoviedb.org/3/movie/" + movieId + "?append_to_response=videos&language=" + language;
        Map<String, Object> stringObjectMap3 = callAPI.callAPI(url3);
        List<String> videoList = extractVideoKeys((Map<String, Object>) stringObjectMap3.get("videos"));

        if (videoList.isEmpty()) {
            language = "en";
            url3 = "https://api.themoviedb.org/3/movie/" + movieId + "?append_to_response=videos&language=" + language;
            stringObjectMap3 = callAPI.callAPI(url3);
            videoList = extractVideoKeys((Map<String, Object>) stringObjectMap3.get("videos"));
        }

        List<ReviewEntity> reviewEntityList = reviewRepository.findByMovieId(movieId);
        double totalReviewScore = 0;
        int reviewCount = 0;

        for (ReviewEntity review : reviewEntityList) {
            Double reviewScore = review.getStar();
            if (reviewScore != null) {
                totalReviewScore += reviewScore;
                reviewCount++;
            }
        }

        double averageReviewScore = reviewCount > 0 ? Math.round(totalReviewScore / reviewCount * 10) / 10.0 : 0.0;

        return MovieDetailDTO.builder()
                .posterImg("https://image.tmdb.org/t/p/original" + stringObjectMap1.get("poster_path"))
                .backGroundImg("https://image.tmdb.org/t/p/original" + stringObjectMap1.get("backdrop_path"))
                .title((String) stringObjectMap1.get("title"))
                .genreDTOList(genreList)
                .overview((String) stringObjectMap1.get("overview"))
                .castDTOList(castList)
                .crewDTOList(crewList)
                .releaseDate((String) stringObjectMap1.get("release_date"))
                .contentRating(contentRating)
                .status((String) stringObjectMap1.get("status"))
                .tmdbScore(Math.round((Double) stringObjectMap1.get("vote_average") * 10) / 10.0)
                .cinetalkScore(averageReviewScore)
                .runningTime((Integer) stringObjectMap1.get("runtime"))
                .imageDTOList(imageList)
                .videoList(videoList)
                .build();
    }

    private List<GenreDTO> extractGenres(List<Map<String, Object>> genresMapList) {
        return genresMapList.stream()
                .map(this::mapToGenreDTO)
                .collect(Collectors.toList());
    }

    private List<CastDTO> extractCast(List<Map<String, Object>> castMapList) {
        return castMapList.stream()
                .map(this::mapToCastDTO)
                .collect(Collectors.toList());
    }

    private List<CrewDTO> extractCrew(List<Map<String, Object>> crewMapList) {
        return crewMapList.stream()
                .map(this::mapToCrewDTO)
                .collect(Collectors.toList());
    }

    private List<ImageDTO> extractImages(List<Map<String, Object>> imagesMapList) {
        return imagesMapList.stream()
                .map(this::mapToImageDTO)
                .collect(Collectors.toList());
    }

    private GenreDTO mapToGenreDTO(Map<String, Object> genreMap) {
        return GenreDTO.builder()
                .id((Integer) genreMap.get("id"))
                .name((String) genreMap.get("name"))
                .build();
    }

    private CastDTO mapToCastDTO(Map<String, Object> castMap) {
        return CastDTO.builder()
                .id((Integer) castMap.get("id"))
                .name((String) castMap.get("name"))
                .character((String) castMap.get("character"))
                .order((Integer) castMap.get("order"))
                .profilePath(castMap.get("profile_path") == null ? null : "https://image.tmdb.org/t/p/original" + castMap.get("profile_path"))
                .build();
    }

    private CrewDTO mapToCrewDTO(Map<String, Object> crewMap) {
        return CrewDTO.builder()
                .id((Integer) crewMap.get("id"))
                .name((String) crewMap.get("name"))
                .job((String) crewMap.get("job"))
                .profilePath(crewMap.get("profile_path") == null ? null : "https://image.tmdb.org/t/p/original" + crewMap.get("profile_path"))
                .build();
    }

    private ImageDTO mapToImageDTO(Map<String, Object> imageMap) {
        return ImageDTO.builder()
                .filePath("https://image.tmdb.org/t/p/original" + imageMap.get("file_path"))
                .aspectRatio((Double) imageMap.get("aspect_ratio"))
                .height((Integer) imageMap.get("height"))
                .width((Integer) imageMap.get("width"))
                .build();
    }

    public String extractContentRating(Map<String, Object> stringObjectMap1) {
        // release_dates 키의 값 확인
        Map<String, Object> releaseDatesMap = (Map<String, Object>) stringObjectMap1.get("release_dates");

        if (releaseDatesMap == null) {
            return "";      // release_dates 데이터가 없는 경우
        }

        // results 키의 값 확인
        List<Map<String, Object>> results = (List<Map<String, Object>>) releaseDatesMap.get("results");

        // results가 null이거나 비어 있는 경우
        if (results == null || results.isEmpty()) {
            return ""; // 연령제한 데이터가 없는 경우
        }

        // 한국(KR)을 찾아 연령 제한 데이터를 확인합니다.
        for (Map<String, Object> result : results) {
            String iso3166_1 = (String) result.get("iso_3166_1");
            if (iso3166_1 != null && iso3166_1.equals("KR")) {
                List<Map<String, Object>> releaseDates = (List<Map<String, Object>>) result.get("release_dates");
                for (Map<String, Object> date : releaseDates) {
                    String certification = (String) date.get("certification");
                    if (certification != null && !certification.isEmpty()) {
                        return certification;
                    }
                }
            }
        }

        return ""; // 한국(KR)의 연령제한 데이터가 없는 경우
    }

    private List<String> extractVideoKeys(Map<String, Object> videosMap) {
        if (videosMap == null) {
            return List.of();
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) videosMap.get("results");

        if (results == null || results.isEmpty()) {
            return List.of();
        }

        return results.stream()
                .map(video -> (String) video.get("key"))
                .collect(Collectors.toList());
    }

}
