package com.back.cinetalk.movie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    //TODO apiCall 하는 api
    public JsonNode CallAPI(String url) throws IOException {
        //1a4fdfbb72ca9489d8eb9487d7a4ccff4434ec32
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhYTdkZDg2MGJkYzJmNzAwNDI2NjcwNmQ4ZGJhYzI1NSIsInN1YiI6IjY1OWJlMzI3YmQ1ODhiMjA5OThkNDI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.TydEZPf9nrIucJSP8WIfQszoJzX9hXJXv2nNTaTIJo4")
                .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode result = objectMapper.readTree(jsonData);

        return result;
    }

    public List<Map<String,Object>> list1() throws IOException {

        String url = "https://api.themoviedb.org/3/movie/top_rated?language=ko-KR&page=1";

        JsonNode rootNode = CallAPI(url);

        JsonNode resultsNode = rootNode.get("results");

        List<Map<String,Object>> movie = new ArrayList<>();

        if (resultsNode.isArray()) {
            for (JsonNode movieNode : resultsNode) {
                String title = movieNode.get("title").asText();
                String overview = movieNode.get("overview").asText();
                String id = movieNode.get("id").asText();
                String poster_path = movieNode.get("poster_path").asText();
                Map<String,Object> map = new HashMap<>();

                map.put("title",title);
                map.put("id",id);
                map.put("overview",overview);
                map.put("poster_path","https://image.tmdb.org/t/p/w500"+poster_path);

                movie.add(map);
            }
        }

        return movie;
    }

    public List<Map<String,Object>> nowPlayingList() throws IOException {

        String url = "https://api.themoviedb.org/3/movie/now_playing?language=ko-KR&page=1";

        JsonNode rootNode = CallAPI(url);

        JsonNode resultsNode = rootNode.get("results");

        List<Map<String,Object>> movie = new ArrayList<>();

        if (resultsNode.isArray()) {
            for (JsonNode movieNode : resultsNode) {
                String title = movieNode.get("title").asText();
                String id = movieNode.get("id").asText();
                String overview = movieNode.get("overview").asText();
                String poster_path = movieNode.get("poster_path").asText();
                Map<String,Object> map = new HashMap<>();

                map.put("title",title);
                map.put("id",id);
                map.put("overview",overview);
                map.put("poster_path","https://image.tmdb.org/t/p/w500"+poster_path);

                movie.add(map);
            }
        }

        return movie;
    }
}
