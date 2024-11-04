package com.back.cinetalk.test;

import kr.co.shineware.nlp.komoran.model.Token;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringTests {

    @Test
    public void test() {

        WebClient webClient = WebClient.create();

        String body = "결말개충격.캐디는끝내줌.베놈이랑에디는도대체무슨사이인가..대만감성체고..명작휴잭맨노래잘한다잔들고웃는짤잭...잭..컴백..이게영화로취급되네..암튼귀여움무지성쿵푸싫어..귀여워이게뭐야실망..오프닝노래하나는끝내줌개꾸르잼~휴잭맨개꾸르잼~~~휴잭맨휴잭맨..이울버린편은잊어줄게..울버린햄볶해져야해휴잭맨";

        body = body.replaceAll("\\.","");

        String requestBody = String.format(
                "{\"document\": {\"content\": \"%s\", \"language\": \"ko-KR\"}, \"encoding_type\": \"UTF8\", \"custom_domain\": \"testDomain\"}",
                body
        );

        Map<String, Object> block = webClient.post()
                .uri("http://localhost:5757/bareun/api/v1/analyze")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", "koba-UTFI4BA-6UAEJDA-V32T4NA-T2XCCBI")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> sentences = (List<Map<String, Object>>) block.get("sentences");

        Map<String, Object> first = sentences.getFirst();

        List<Map<String, Object>> tokens = (List<Map<String, Object>>) first.get("tokens");

        Map<String, Object> first1 = tokens.getFirst();

        List<Map<String, Object>> morphemes = (List<Map<String, Object>>) first1.get("morphemes");

        Map<String, Integer> wordFrequency = new HashMap<>();
        for (Map<String, Object> morpheme : morphemes) {

            Map<String, Object> text = (Map<String, Object>) morpheme.get("text");
            String content = (String) text.get("content");
            String tag = (String) morpheme.get("tag");


            if(tag.contains("NN")){
                System.out.println(content);
                System.out.println(tag);
                System.out.println("------------------------------");
                wordFrequency.put(content, wordFrequency.getOrDefault(content, 0) + 1);
            }
        }

        // 빈도순으로 정렬
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFrequency.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println(sortedList);
    }
}
