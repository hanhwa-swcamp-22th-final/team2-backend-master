package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "인코텀 수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIncotermRequest {
    @Schema(description = "인코텀 코드", example = "FOB")
    private String incotermCode;
    @Schema(description = "인코텀명 (영문)", example = "Free On Board")
    private String incotermName;
    @Schema(description = "인코텀명 (한글)", example = "본선인도조건")
    private String incotermNameKr;
    @Schema(description = "인코텀 설명", example = "매도인이 지정 선적항에서 본선에 물품을 적재")
    private String incotermDescription;
    @Schema(description = "운송 방식", example = "해상운송")
    private String incotermTransportMode;
    @Schema(description = "매도인 부담 구간", example = "선적항 본선까지")
    private String incotermSellerSegments;
    @Schema(description = "기본 지정 장소", example = "부산항")
    private String incotermDefaultNamedPlace;
}
