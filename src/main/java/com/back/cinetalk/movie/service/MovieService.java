package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.MovieDTO;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final getNewMovie getNewMovie;

    private final MovieRepository movieRepository;

    @SuppressWarnings("unchecked")
    public Map<String,Object> CallAPI(String url) throws IOException{

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

        List<Map<String,Object>> result = new ArrayList<>();

        if(lists.isEmpty()){

            List<String> nowPlayingName = getNewMovie.MainList();

            for (String query : nowPlayingName) {

                Map<String,Object> map = getOneByName(query);

                if(map != null){

                    MovieDTO movieDTO = new MovieDTO();

                    movieDTO.setMovie_id(String.valueOf(map.get("id")));
                    movieDTO.setMovienm((String) map.get("title"));

                    MovieEntity movieEntity = MovieEntity.ToMovieEntity(movieDTO);

                    movieRepository.save(movieEntity);

                    result.add(map);
                }
            }
            //map.put("poster_path","https://image.tmdb.org/t/p/w500"+poster_path);
        }else{

            for (MovieEntity movieEntity : lists) {

                result.add(getOneByID(movieEntity.getMovie_id()));
            }
        }

        return result;
    }

    public Map<String, Object> getOneByName(String query) throws IOException {

        log.info("query : "+query);

        String url = "https://api.themoviedb.org/3/search/movie?include_adult=true&language=ko&page=1&query="+query;

        Map<String, Object> responsebody = CallAPI(url);

        List<Map<String,Object>> resultList = (List<Map<String, Object>>) responsebody.get("results");

        if(!resultList.isEmpty()){
            return resultList.get(0);
        }else {
            return null;
        }
    }

    public Map<String, Object> getOneByID(String movie_id) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/"+movie_id+"?language=ko";

        return CallAPI(url);
    }
}
