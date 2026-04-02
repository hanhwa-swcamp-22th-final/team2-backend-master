package com.team2.master.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientListResponse {
    private Integer clientId;
    private String clientCode;
    private String clientName;
    private String clientNameKr;
    private String countryName;
    private String clientCity;
    private Integer departmentId;
    private String clientStatus;
    private LocalDate clientRegDate;
}
