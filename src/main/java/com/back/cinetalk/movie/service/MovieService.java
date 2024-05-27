package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.*;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final getNewMovie getNewMovie;

    private final MovieRepository movieRepository;

    @SuppressWarnings("unchecked")
    public Map<String, Object> CallAPI(String url) throws IOException {

        WebClient webClient = WebClient.create();

        return (Map<String, Object>) webClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhYTdkZDg2MGJkYzJmNzAwNDI2NjcwNmQ4ZGJhYzI1NSIsInN1YiI6IjY1OWJlMzI3YmQ1ODhiMjA5OThkNDI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.TydEZPf9nrIucJSP8WIfQszoJzX9hXJXv2nNTaTIJo4")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public List<Map<String, Object>> nowPlayingList() throws IOException {

        LocalDate today = LocalDate.now();

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //String yesterdayFormatted = today.format(formatter);

        System.out.println(today);

        List<MovieEntity> lists = movieRepository.findByRegdate(today);

        List<Map<String, Object>> result = new ArrayList<>();

        if (lists.isEmpty()) {

            List<String> nowPlayingName = getNewMovie.MainList();
            for (String query : nowPlayingName) {

                Map<String, Object> map = getOneByName(query);

                if (map != null) {

                    MovieDTO movieDTO = new MovieDTO();

                    movieDTO.setMovie_id(String.valueOf(map.get("id")));
                    movieDTO.setMovienm((String) map.get("title"));

                    MovieEntity movieEntity = MovieEntity.ToMovieEntity(movieDTO);

                    movieRepository.save(movieEntity);

                    result.add(map);
                }
            }
            //map.put("poster_path","https://image.tmdb.org/t/p/w500"+poster_path);
        } else {

            for (MovieEntity movieEntity : lists) {
                result.add(getOneByID(movieEntity.getMovie_id()));
            }
        }

        return result;
    }

    public Map<String, Object> getOneByName(String query) throws IOException {

        log.info("query : " + query);

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query=" + query;

        Map<String, Object> responsebody = CallAPI(url);

        log.info("responsebody = " + responsebody);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) responsebody.get("results");

        log.info("resultList = " + resultList);
        if (!resultList.isEmpty()) {
            System.out.println("resultList.get(0) = " + resultList.get(0));
            return resultList.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Object> getOneByID(String movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?language=ko";

        return CallAPI(url);
    }

    // 영화 상세 정보
    public MovieDetailDTO getMovieDetail(String movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?append_to_response=credits,release_dates&language=ko";
        Map<String, Object> stringObjectMap = CallAPI(url);

        List<GenreDTO> genres = extractGenres((List<Map<String, Object>>) stringObjectMap.get("genres"));
        List<CastDTO> cast = extractCast((List<Map<String, Object>>) ((Map<String, Object>) stringObjectMap.get("credits")).get("cast"));
        List<CrewDTO> crew = extractCrew((List<Map<String, Object>>) ((Map<String, Object>) stringObjectMap.get("credits")).get("crew"));
        String contentRating = extractContentRating(stringObjectMap);

        return MovieDetailDTO.builder()
                .posterImg((String) stringObjectMap.get("poster_path"))
                .backGroundImg((String) stringObjectMap.get("backdrop_path"))
                .title((String) stringObjectMap.get("title"))
                .genreDTOList(genres)
                .overview((String) stringObjectMap.get("overview"))
                .castDTOList(cast)
                .crewDTOList(crew)
                .releaseDate((String) stringObjectMap.get("release_date"))
                .contentRating(contentRating)
                .status((String) stringObjectMap.get("status"))
                .score(Math.round((Double) stringObjectMap.get("vote_average") * 10) / 10.0)
                .runningTime((Integer) stringObjectMap.get("runtime"))
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
                .profilePath((String) castMap.get("profile_path"))
                .build();
    }

    private CrewDTO mapToCrewDTO(Map<String, Object> crewMap) {
        return CrewDTO.builder()
                .id((Integer) crewMap.get("id"))
                .name((String) crewMap.get("name"))
                .job((String) crewMap.get("job"))
                .profilePath((String) crewMap.get("profile_path"))
                .build();
    }

    private String extractContentRating(Map<String, Object> stringObjectMap) {
        // release_dates 키의 값 확인
        Map<String, Object> releaseDatesMap = (Map<String, Object>) stringObjectMap.get("release_dates");

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

}
