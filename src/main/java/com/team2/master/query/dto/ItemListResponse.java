package com.team2.master.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemListResponse {
    private Integer itemId;
    private String itemCode;
    private String itemName;
    private String itemNameKr;
    private String itemSpec;
    private String itemUnit;
    private BigDecimal itemUnitPrice;
    private String itemCategory;
    private String itemStatus;
}
