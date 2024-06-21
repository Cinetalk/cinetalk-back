package com.back.cinetalk.movie.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class getNewMovie {

    private final WebClient webClient;

    private String key = "b0eaad9be154d293c5c38849e83705a7";

    public getNewMovie(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice")
                .build();
    }
    // 이름이랑 누적관객수만 가져옴
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> MainList() {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String yesterdayFormatted = yesterday.format(formatter);

        Map<String, Object> responseBody = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/rest/boxoffice/searchDailyBoxOfficeList.json")
                        .queryParam("key", key)
                        .queryParam("targetDt", yesterdayFormatted)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String,Object> result = (Map<String, Object>) responseBody.get("boxOfficeResult");

        List<Map<String,Object>> list = (List<Map<String, Object>>) result.get("dailyBoxOfficeList");

        List<Map<String,Object>> resultlist = new ArrayList<>();

        for (Map<String,Object> map : list) {

            HashMap<String, Object> resultmap = new HashMap<>();

            resultmap.put("movieNm",map.get("movieNm"));
            resultmap.put("audiAcc",map.get("audiAcc"));

            resultlist.add(resultmap);
        }

        // 성공 메시지 반환
        return resultlist;
    }
}
