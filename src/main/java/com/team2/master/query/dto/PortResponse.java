package com.team2.master.query.dto;

import com.team2.master.command.domain.entity.Port;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "항구 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortResponse {
    @Schema(description = "항구 ID", example = "1")
    private Integer id;
    @Schema(description = "항구 코드", example = "KRPUS")
    private String portCode;
    @Schema(description = "항구명", example = "Busan Port")
    private String portName;
    @Schema(description = "항구 소재 도시", example = "Busan")
    private String portCity;
    @Schema(description = "국가 ID", example = "1")
    private Integer countryId;
    @Schema(description = "국가명", example = "South Korea")
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
