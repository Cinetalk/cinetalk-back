package com.back.cinetalk.find.service;

import com.back.cinetalk.movie.service.CallAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdultContentFinder {

    private final CallAPI callAPI;

    public Boolean adultFinder(Long movieId) throws IOException {

        String url = "https://api.themoviedb.org/3/movie/"+movieId+"/keywords";

        List<Map<String,Object>> kewordList = (List<Map<String, Object>>) callAPI.callAPI(url).get("keywords");

        if(kewordList.isEmpty()){
            return false;
        }

        for (Map<String,Object> keword : kewordList) {

            int id = (int) keword.get("id");

            if(id == 155477){
                return true;
            }
        }
        return false;
    }
}
