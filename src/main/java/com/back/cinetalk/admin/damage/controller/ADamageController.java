package com.back.cinetalk.admin.damage.controller;

import com.back.cinetalk.admin.damage.service.ADamageService;
import com.back.cinetalk.report.dto.ReportRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/adamage")
@RestController
@RequiredArgsConstructor
public class ADamageController {

    private final ADamageService adamageService;

    @PostMapping("/{report_id}")
    public ResponseEntity<?> ADamage(@PathVariable("report_id") Long report_id, ReportRequestDTO requestDTO) {

        return adamageService.ADamage(report_id,requestDTO);
    }

}
