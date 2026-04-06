package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "통화 수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCurrencyRequest {
    @Schema(description = "통화 코드", example = "USD")
    private String currencyCode;
    @Schema(description = "통화명", example = "US Dollar")
    private String currencyName;
    @Schema(description = "통화 기호", example = "$")
    private String currencySymbol;
}
