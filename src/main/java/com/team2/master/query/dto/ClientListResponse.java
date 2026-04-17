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

    public Integer getId() { return clientId; }
    public String getName() { return clientName; }
    public String getNameKr() { return clientNameKr; }
    @Schema(description = "국가명", example = "South Korea")
    private String countryName;
    @Schema(description = "도시명", example = "Seoul")
    private String clientCity;
    @Schema(description = "팀 ID", example = "1")
    private Integer teamId;
    @Schema(description = "팀 이름", example = "영업1팀")
    private String teamName;
    @Schema(description = "부서 ID", example = "1")
    private Integer departmentId;
    @Schema(description = "부서 이름", example = "영업부")
    private String departmentName;
    @Schema(description = "거래처 상태", example = "ACTIVE")
    private String clientStatus;
    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate clientRegDate;
    @Schema(description = "항구명", example = "Busan Port")
    private String portName;
    @Schema(description = "담당자명", example = "홍길동")
    private String clientManager;
    @Schema(description = "결제조건 ID", example = "1")
    private Integer paymentTermsId;
    @Schema(description = "통화 ID", example = "1")
    private Integer currencyId;
    @Schema(description = "주소", example = "123 Main St, Seoul")
    private String clientAddress;
    @Schema(description = "전화번호", example = "+82-2-1234-5678")
    private String clientTel;
    @Schema(description = "이메일", example = "contact@abc.com")
    private String clientEmail;
}
