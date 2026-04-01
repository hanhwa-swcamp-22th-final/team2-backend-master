package com.team2.master.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCurrencyRequest {
    private String currencyCode;
    private String currencyName;
    private String currencySymbol;
}
