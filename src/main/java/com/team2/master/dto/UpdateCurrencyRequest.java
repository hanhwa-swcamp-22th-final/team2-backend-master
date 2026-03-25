package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCurrencyRequest {
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
}
