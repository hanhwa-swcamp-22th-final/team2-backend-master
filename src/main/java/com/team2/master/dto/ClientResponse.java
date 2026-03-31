package com.team2.master.dto;

import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ClientResponse {
    private Integer id;
    private String clientCode;
    private String clientName;
    private String clientNameKr;
    private Integer countryId;
    private String countryName;
    private String clientCity;
    private Integer portId;
    private String portName;
    private String clientAddress;
    private String clientTel;
    private String clientEmail;
    private Integer paymentTermId;
    private String paymentTermName;
    private Integer currencyId;
    private String currencyName;
    private String clientManager;
    private Integer departmentId;
    private ClientStatus clientStatus;
    private LocalDate clientRegDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
