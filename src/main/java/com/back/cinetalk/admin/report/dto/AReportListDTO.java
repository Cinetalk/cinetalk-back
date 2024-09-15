package com.back.cinetalk.admin.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AReportListDTO {

    private Long id;
    private String category;
    private String content;
    private boolean status;
    private String review_content;
    private String user_email;
    private String user_nickName;
    private LocalDateTime createdAt;
}
