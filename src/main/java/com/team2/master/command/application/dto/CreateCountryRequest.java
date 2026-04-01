package com.team2.master.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCountryRequest {
    @NotBlank(message = "국가 코드는 필수입니다.")
    private String countryCode;
    @NotBlank(message = "국가명은 필수입니다.")
    private String countryName;
    private String countryNameKr;
}
