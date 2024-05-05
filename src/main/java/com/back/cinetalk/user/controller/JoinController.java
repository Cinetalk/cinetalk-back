package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpResponse;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {

        this.joinService = joinService;
    }

    @Operation(summary = "회원가입 프로세스",description = "회원가입 처리하는 로직 지금은 사용 X")
    @ApiResponse(responseCode = "200",description = "가입 완료",content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    @PostMapping("/join")
    public String joinProcess(UserDTO userDTO) {

        joinService.joinProcess(userDTO);

        return "ok";
    }

}
