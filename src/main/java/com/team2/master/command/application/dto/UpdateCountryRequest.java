package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "국가 수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryRequest {
    @Schema(description = "국가 코드", example = "KR")
    private String countryCode;
    @Schema(description = "국가명 (영문)", example = "South Korea")
    private String countryName;
    @Schema(description = "국가명 (한글)", example = "대한민국")
    private String countryNameKr;
}
