package com.team2.master.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientRequest {
    private String clientName;
    private String clientNameKr;
    private Integer countryId;
    private String clientCity;
    private Integer portId;
    private String clientAddress;
    private String clientTel;
    private String clientEmail;
    private Integer paymentTermId;
    private Integer currencyId;
    private String clientManager;
    private Integer departmentId;
}
