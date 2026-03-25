package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentTermRequest {
    private String paymentTermCode;
    private String paymentTermName;
    private String paymentTermDescription;
}
