package com.team2.master.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {
    private String itemName;
    private String itemNameKr;
    private String itemSpec;
    private String itemUnit;
    private String itemPackUnit;
    private BigDecimal itemUnitPrice;
    private BigDecimal itemWeight;
    private String itemHsCode;
    private String itemCategory;
}
