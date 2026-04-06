package com.team2.master.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(description = "거래처 목록 응답")
@Getter
@Setter
public class ClientListResponse {
    @Schema(description = "거래처 ID", example = "1")
    private Integer clientId;
    @Schema(description = "거래처 코드", example = "CLI-001")
    private String clientCode;
    @Schema(description = "거래처명 (영문)", example = "ABC Trading Co.")
    private String clientName;
    @Schema(description = "거래처명 (한글)", example = "ABC 무역")
    private String clientNameKr;
    @Schema(description = "국가명", example = "South Korea")
    private String countryName;
    @Schema(description = "도시명", example = "Seoul")
    private String clientCity;
    @Schema(description = "부서 ID", example = "1")
    private Integer departmentId;
    @Schema(description = "거래처 상태", example = "ACTIVE")
    private String clientStatus;
    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate clientRegDate;
}
