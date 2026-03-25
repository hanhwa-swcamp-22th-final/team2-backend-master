package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBuyerRequest {
    private String buyerName;
    private String buyerPosition;
    private String buyerEmail;
    private String buyerTel;
}
