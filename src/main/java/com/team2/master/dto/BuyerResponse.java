package com.team2.master.dto;

import com.team2.master.entity.Buyer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BuyerResponse {
    private Integer id;
    private Integer clientId;
    private String clientName;
    private String buyerName;
    private String buyerPosition;
    private String buyerEmail;
    private String buyerTel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BuyerResponse from(Buyer buyer) {
        return BuyerResponse.builder()
                .id(buyer.getId())
                .clientId(buyer.getClient().getId())
                .clientName(buyer.getClient().getClientName())
                .buyerName(buyer.getBuyerName())
                .buyerPosition(buyer.getBuyerPosition())
                .buyerEmail(buyer.getBuyerEmail())
                .buyerTel(buyer.getBuyerTel())
                .createdAt(buyer.getCreatedAt())
                .updatedAt(buyer.getUpdatedAt())
                .build();
    }
}
