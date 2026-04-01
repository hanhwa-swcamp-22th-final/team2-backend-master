package com.team2.master.query.dto;

import com.team2.master.command.domain.entity.Buyer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
                .id(buyer.getBuyerId())
                .clientId(buyer.getClient().getClientId())
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
