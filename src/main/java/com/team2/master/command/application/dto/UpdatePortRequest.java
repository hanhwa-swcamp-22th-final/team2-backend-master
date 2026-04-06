package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "항구 수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePortRequest {
    @Schema(description = "항구 코드", example = "KRPUS")
    private String portCode;
    @Schema(description = "항구명", example = "Busan Port")
    private String portName;
    @Schema(description = "항구 소재 도시", example = "Busan")
    private String portCity;
    @Schema(description = "국가 ID", example = "1")
    private Integer countryId;
}
