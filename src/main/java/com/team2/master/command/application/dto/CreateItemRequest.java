package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "품목 등록 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {
    @Schema(description = "품목 코드", example = "ITM-001")
    private String itemCode;
    @Schema(description = "품목명 (영문)", example = "Steel Plate")
    private String itemName;
    @Schema(description = "품목명 (한글)", example = "철판")
    private String itemNameKr;
    @Schema(description = "품목 규격 (표시용 조립 문자열, 선택)", example = "100 × 200 × 300 mm")
    private String itemSpec;
    @Schema(description = "규격 — 너비(mm)", example = "100")
    private Integer itemWidth;
    @Schema(description = "규격 — 깊이(mm)", example = "200")
    private Integer itemDepth;
    @Schema(description = "규격 — 높이(mm)", example = "300")
    private Integer itemHeight;
    @Schema(description = "단위", example = "EA")
    private String itemUnit;
    @Schema(description = "포장 단위", example = "BOX")
    private String itemPackUnit;
    @Schema(description = "단가", example = "150.00")
    private BigDecimal itemUnitPrice;
    @Schema(description = "중량 (kg)", example = "25.5")
    private BigDecimal itemWeight;
    @Schema(description = "HS 코드", example = "7208.51")
    private String itemHsCode;
    @Schema(description = "품목 카테고리", example = "원자재")
    private String itemCategory;
    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate itemRegDate;
}
