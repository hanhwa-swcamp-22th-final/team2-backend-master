package com.team2.master.query.dto;

import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "거래처 상세 응답")
@Getter
@Setter
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
    @Schema(description = "팀 ID", example = "1")
    private Integer teamId;
    @Schema(description = "팀 이름", example = "영업1팀")
    private String teamName;
    @Schema(description = "부서 ID (팀 경유 조회)", example = "1")
    private Integer departmentId;
    @Schema(description = "부서 이름", example = "영업부")
    private String departmentName;
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
    // 프론트엔드/외부 소비자가 clientId 를 기대하므로 JSON 에 id 와 clientId 둘 다 내려간다.
    // id 만 있던 시절엔 PO/CI/PL 생성 시 clientId=null → 0 으로 떨어져 메일 발송·활동-PO 연결이
    // 전부 깨졌음 (2026-04-21 시연 블로커). Lombok @Getter 가 생성한 getId() 는 그대로 둔다.
    public Integer getClientId() { return id; }

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
                .teamId(client.getTeamId())
                .clientStatus(client.getClientStatus())
                .clientRegDate(client.getClientRegDate())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }
}
