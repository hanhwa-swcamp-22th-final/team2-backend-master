package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "거래처 수정 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientRequest {
    @Schema(description = "거래처명 (영문)", example = "ABC Trading Co.")
    private String clientName;
    @Schema(description = "거래처명 (한글)", example = "ABC 무역")
    private String clientNameKr;
    @Schema(description = "국가 ID", example = "1")
    private Integer countryId;
    @Schema(description = "도시명", example = "Seoul")
    private String clientCity;
    @Schema(description = "항구 ID", example = "1")
    private Integer portId;
    @Schema(description = "주소", example = "123 Main St")
    private String clientAddress;
    @Schema(description = "전화번호", example = "02-1234-5678")
    private String clientTel;
    @Schema(description = "이메일", example = "info@abc.com")
    private String clientEmail;
    @Schema(description = "결제조건 ID", example = "1")
    private Integer paymentTermId;
    @Schema(description = "통화 ID", example = "1")
    private Integer currencyId;
    @Schema(description = "담당자명", example = "홍길동")
    private String clientManager;
    @Schema(description = "부서 ID", example = "1")
    private Integer departmentId;
}
