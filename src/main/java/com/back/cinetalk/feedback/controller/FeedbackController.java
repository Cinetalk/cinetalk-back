package com.back.cinetalk.feedback.controller;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.feedback.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/save")
    public StateRes FeedbackSave(HttpServletRequest request, @RequestParam(value = "content")String content){
        return feedbackService.FeedbackSave(request, content);
    }
}
