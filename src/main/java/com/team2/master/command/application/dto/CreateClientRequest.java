package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "거래처 등록 요청")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {
    @Schema(description = "거래처 코드 (미제공 시 서버 자동 생성)", example = "CLI-001")
    private String clientCode;

    @NotBlank(message = "영문 거래처명은 필수입니다.")
    @Size(max = 100, message = "거래처명은 100자 이내로 입력하세요.")
    @Schema(description = "거래처명 (영문)", example = "ABC Trading Co.")
    private String clientName;

    @Size(max = 50, message = "한글 거래처명은 50자 이내로 입력하세요.")
    @Schema(description = "거래처명 (한글)", example = "ABC 무역")
    private String clientNameKr;

    @NotNull(message = "국가는 필수입니다.")
    @Schema(description = "국가 ID", example = "1")
    private Integer countryId;

    @Size(max = 50)
    @Schema(description = "도시명", example = "Seoul")
    private String clientCity;

    @NotNull(message = "도착항은 필수입니다.")
    @Schema(description = "항구 ID", example = "1")
    private Integer portId;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 200, message = "주소는 200자 이내로 입력하세요.")
    @Schema(description = "주소", example = "123 Main St")
    private String clientAddress;

    @NotBlank(message = "거래처 연락처는 필수입니다.")
    @Pattern(regexp = "^\\+?[\\d\\s()-]{7,20}$", message = "올바른 전화번호 형식을 입력하세요.")
    @Schema(description = "전화번호", example = "+82 02-1234-5678")
    private String clientTel;

    @NotBlank(message = "거래처 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    @Size(max = 100)
    @Schema(description = "이메일", example = "info@abc.com")
    private String clientEmail;

    @NotNull(message = "결제조건은 필수입니다.")
    @Schema(description = "결제조건 ID", example = "1")
    private Integer paymentTermId;

    @NotNull(message = "통화는 필수입니다.")
    @Schema(description = "통화 ID", example = "1")
    private Integer currencyId;

    @NotBlank(message = "거래처 담당자(바이어)는 필수입니다.")
    @Size(max = 50)
    @Schema(description = "거래처 담당자(바이어)", example = "Mr. Ahmad Razak")
    private String clientManager;

    @Schema(description = "부서 ID", example = "1")
    private Integer departmentId;

    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate clientRegDate;
}
