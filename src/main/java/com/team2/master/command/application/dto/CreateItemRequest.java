package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Schema(description = "품목 코드 (미제공 시 서버 자동 생성)", example = "ITM-001")
    private String itemCode;

    @NotBlank(message = "품목명은 필수입니다.")
    @Size(max = 100, message = "품목명은 100자 이내로 입력하세요.")
    @Schema(description = "품목명 (영문)", example = "Steel Plate")
    private String itemName;

    @Size(max = 100)
    @Schema(description = "품목명 (한글)", example = "철판")
    private String itemNameKr;

    @Size(max = 200)
    @Schema(description = "품목 규격 (표시용 조립 문자열)", example = "100 × 200 × 300 mm")
    private String itemSpec;

    @NotNull(message = "너비는 필수입니다.")
    @Min(value = 1, message = "너비는 1mm 이상입니다.")
    @Max(value = 99999, message = "너비는 99,999mm 이하입니다.")
    @Schema(description = "규격 — 너비(mm)", example = "100")
    private Integer itemWidth;

    @NotNull(message = "깊이는 필수입니다.")
    @Min(value = 1, message = "깊이는 1mm 이상입니다.")
    @Max(value = 99999, message = "깊이는 99,999mm 이하입니다.")
    @Schema(description = "규격 — 깊이(mm)", example = "200")
    private Integer itemDepth;

    @NotNull(message = "높이는 필수입니다.")
    @Min(value = 1, message = "높이는 1mm 이상입니다.")
    @Max(value = 99999, message = "높이는 99,999mm 이하입니다.")
    @Schema(description = "규격 — 높이(mm)", example = "300")
    private Integer itemHeight;

    @NotBlank(message = "단위는 필수입니다.")
    @Schema(description = "단위", example = "EA")
    private String itemUnit;

    @Schema(description = "포장 단위", example = "BOX")
    private String itemPackUnit;

    @NotNull(message = "단가는 필수입니다.")
    @DecimalMin(value = "0.01", message = "단가는 0보다 커야 합니다.")
    @Schema(description = "단가", example = "150.00")
    private BigDecimal itemUnitPrice;

    @DecimalMin(value = "0", message = "중량은 0 이상입니다.")
    @Schema(description = "중량 (kg)", example = "25.5")
    private BigDecimal itemWeight;

    @Pattern(regexp = "^$|^[\\d.]+$", message = "HS Code는 숫자와 점(.)만 입력 가능합니다.")
    @Size(max = 20)
    @Schema(description = "HS 코드", example = "7208.51")
    private String itemHsCode;

    @Size(max = 100)
    @Schema(description = "품목 카테고리", example = "원자재")
    private String itemCategory;

    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate itemRegDate;
}
