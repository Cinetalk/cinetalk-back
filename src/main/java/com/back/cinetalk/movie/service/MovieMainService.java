package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final getNewMovie getNewMovie;
    private final MovieRepository movieRepository;
    private final CallAPI callAPI;

    public List<Map<String, Object>> nowPlayingList() throws IOException {

        LocalDate today = LocalDate.now();

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

        Map<String, Object> responsebody = callAPI.callAPI(url);

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

        return callAPI.callAPI(url);
    }
}
