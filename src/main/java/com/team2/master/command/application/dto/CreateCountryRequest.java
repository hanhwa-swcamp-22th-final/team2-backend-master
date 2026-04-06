package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "국가 등록 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCountryRequest {
    @Schema(description = "국가 코드", example = "KR")
    @NotBlank(message = "국가 코드는 필수입니다.")
    private String countryCode;
    @Schema(description = "국가명 (영문)", example = "South Korea")
    @NotBlank(message = "국가명은 필수입니다.")
    private String countryName;
    @Schema(description = "국가명 (한글)", example = "대한민국")
    private String countryNameKr;
}
