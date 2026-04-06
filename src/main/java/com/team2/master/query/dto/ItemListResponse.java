package com.team2.master.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "품목 목록 응답")
@Getter
@Setter
public class ItemListResponse {
    @Schema(description = "품목 ID", example = "1")
    private Integer itemId;
    @Schema(description = "품목 코드", example = "ITM-001")
    private String itemCode;
    @Schema(description = "품목명 (영문)", example = "Steel Plate")
    private String itemName;
    @Schema(description = "품목명 (한글)", example = "철판")
    private String itemNameKr;
    @Schema(description = "품목 규격", example = "10mm x 1000mm x 2000mm")
    private String itemSpec;
    @Schema(description = "단위", example = "EA")
    private String itemUnit;
    @Schema(description = "단가", example = "150.00")
    private BigDecimal itemUnitPrice;
    @Schema(description = "품목 카테고리", example = "원자재")
    private String itemCategory;
    @Schema(description = "품목 상태", example = "ACTIVE")
    private String itemStatus;
}
