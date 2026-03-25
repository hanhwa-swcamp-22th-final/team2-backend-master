package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {
    private String itemCode;
    private String itemName;
    private String itemNameKr;
    private String itemSpec;
    private String itemUnit;
    private String itemPackUnit;
    private BigDecimal itemUnitPrice;
    private BigDecimal itemWeight;
    private String itemHsCode;
    private String itemCategory;
    private LocalDate itemRegDate;
}
