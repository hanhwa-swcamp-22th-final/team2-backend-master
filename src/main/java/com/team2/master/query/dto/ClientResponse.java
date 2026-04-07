package com.team2.master.query.dto;

import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "거래처 상세 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    @Schema(description = "거래처 ID", example = "1")
    private Integer id;
    @Schema(description = "거래처 코드", example = "CLI-001")
    private String clientCode;
    @Schema(description = "거래처명 (영문)", example = "ABC Trading Co.")
    private String clientName;
    @Schema(description = "거래처명 (한글)", example = "ABC 무역")
    private String clientNameKr;
    @Schema(description = "국가 ID", example = "1")
    private Integer countryId;
    @Schema(description = "국가명", example = "South Korea")
    private String countryName;
    @Schema(description = "도시명", example = "Seoul")
    private String clientCity;
    @Schema(description = "항구 ID", example = "1")
    private Integer portId;
    @Schema(description = "항구명", example = "Busan Port")
    private String portName;
    @Schema(description = "주소", example = "123 Main St")
    private String clientAddress;
    @Schema(description = "전화번호", example = "02-1234-5678")
    private String clientTel;
    @Schema(description = "이메일", example = "info@abc.com")
    private String clientEmail;
    @Schema(description = "결제조건 ID", example = "1")
    private Integer paymentTermId;
    @Schema(description = "결제조건명", example = "T/T 30 Days")
    private String paymentTermName;
    @Schema(description = "통화 ID", example = "1")
    private Integer currencyId;
    @Schema(description = "통화명", example = "US Dollar")
    private String currencyName;
    @Schema(description = "담당자명", example = "홍길동")
    private String clientManager;
    @Schema(description = "부서 ID", example = "1")
    private Integer departmentId;
    @Schema(description = "거래처 상태", example = "ACTIVE")
    private ClientStatus clientStatus;
    @Schema(description = "등록일", example = "2026-01-01")
    private LocalDate clientRegDate;
    @Schema(description = "생성일시")
    private LocalDateTime createdAt;
    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public String getName() { return clientName; }
    public String getNameKr() { return clientNameKr; }
    public Integer getPaymentTermsId() { return paymentTermId; }
    public LocalDate getRegDate() { return clientRegDate; }

    public static ClientResponse from(Client client) {
        return ClientResponse.builder()
                .id(client.getClientId())
                .clientCode(client.getClientCode())
                .clientName(client.getClientName())
                .clientNameKr(client.getClientNameKr())
                .countryId(client.getCountry() != null ? client.getCountry().getCountryId() : null)
                .countryName(client.getCountry() != null ? client.getCountry().getCountryName() : null)
                .clientCity(client.getClientCity())
                .portId(client.getPort() != null ? client.getPort().getPortId() : null)
                .portName(client.getPort() != null ? client.getPort().getPortName() : null)
                .clientAddress(client.getClientAddress())
                .clientTel(client.getClientTel())
                .clientEmail(client.getClientEmail())
                .paymentTermId(client.getPaymentTerm() != null ? client.getPaymentTerm().getPaymentTermId() : null)
                .paymentTermName(client.getPaymentTerm() != null ? client.getPaymentTerm().getPaymentTermName() : null)
                .currencyId(client.getCurrency() != null ? client.getCurrency().getCurrencyId() : null)
                .currencyName(client.getCurrency() != null ? client.getCurrency().getCurrencyName() : null)
                .clientManager(client.getClientManager())
                .departmentId(client.getDepartmentId())
                .clientStatus(client.getClientStatus())
                .clientRegDate(client.getClientRegDate())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }
}
