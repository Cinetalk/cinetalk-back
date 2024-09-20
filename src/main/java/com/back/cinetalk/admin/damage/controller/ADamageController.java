package com.back.cinetalk.admin.damage.controller;

import com.back.cinetalk.admin.damage.dto.DamageRequestDTO;
import com.back.cinetalk.admin.damage.service.ADamageService;
import com.back.cinetalk.config.dto.StateRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/adamage")
@RestController
@RequiredArgsConstructor
public class ADamageController {

    private final ADamageService adamageService;

    @PostMapping("/{report_id}")
    @Operation(summary = "관리자: 제재 처리 ",description = "오늘을 기준으로 보낸 일 수 만큼의 제재정보를 기입하는 처리")
    @ApiResponse(responseCode = "200",description = "처리 완료",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes ADamage(@PathVariable("report_id") Long report_id, DamageRequestDTO requestDTO) {

        return adamageService.ADamage(report_id,requestDTO);
    }

    @DeleteMapping("/delete/{report_id}")
    @Operation(summary = "관리자: 댓글 삭제 ",description = "해당 신고의 댓글을 삭제시키는 처리")
    @ApiResponse(responseCode = "200",description = "처리 완료",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes DeleteReview(@PathVariable("report_id") Long report_id){

        return adamageService.DeleteReview(report_id);
    }

}
