package com.back.cinetalk.movie.controller;

import com.back.cinetalk.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/list")
    public List<Map<String,Object>> list() throws IOException {

        return movieService.list1();
    }

    @PostMapping("/MainList")
    public ResponseEntity<?> MainList(){

        return movieService.MainList();
    }
}
