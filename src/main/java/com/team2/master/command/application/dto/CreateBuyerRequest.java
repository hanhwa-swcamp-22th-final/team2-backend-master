package com.team2.master.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuyerRequest {
    private Integer clientId;
    private String buyerName;
    private String buyerPosition;
    private String buyerEmail;
    private String buyerTel;
}
