package com.back.cinetalk.find.controller;

import com.back.cinetalk.find.service.FindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/find")
@RequiredArgsConstructor
public class FindController {

    private final FindService findService;

    @PostMapping("/WordSave")
    public ResponseEntity<?> WordSave(@RequestParam(value = "keword") String keword){

        return findService.WordSave(keword);
    }
}
