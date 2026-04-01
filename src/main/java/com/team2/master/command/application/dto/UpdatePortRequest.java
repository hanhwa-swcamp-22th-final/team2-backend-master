package com.team2.master.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePortRequest {
    private String portCode;
    private String portName;
    private String portCity;
    private Integer countryId;
}
