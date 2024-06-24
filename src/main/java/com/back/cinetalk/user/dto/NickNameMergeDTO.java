package com.back.cinetalk.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Schema(description = "닉네임 수정을 위한 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NickNameMergeDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    private String gender;

    @NotNull
    private LocalDate birthday;
}
