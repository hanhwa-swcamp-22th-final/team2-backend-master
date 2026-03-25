package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryRequest {
    private String countryCode;
    private String countryName;
    private String countryNameKr;
}
