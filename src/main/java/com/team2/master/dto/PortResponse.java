package com.team2.master.dto;

import com.team2.master.entity.Port;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortResponse {
    private Integer id;
    private String portCode;
    private String portName;
    private String portCity;
    private Integer countryId;
    private String countryName;

    public static PortResponse from(Port port) {
        return PortResponse.builder()
                .id(port.getPortId())
                .portCode(port.getPortCode())
                .portName(port.getPortName())
                .portCity(port.getPortCity())
                .countryId(port.getCountry().getCountryId())
                .countryName(port.getCountry().getCountryName())
                .build();
    }
}
