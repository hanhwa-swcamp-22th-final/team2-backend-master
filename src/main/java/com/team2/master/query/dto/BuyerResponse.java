package com.team2.master.query.dto;

import com.team2.master.command.domain.entity.Buyer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "바이어 응답")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerResponse {
    @Schema(description = "바이어 ID", example = "1")
    private Integer id;
    @Schema(description = "거래처 ID", example = "1")
    private Integer clientId;
    @Schema(description = "거래처명", example = "ABC Trading Co.")
    private String clientName;
    @Schema(description = "바이어명", example = "John Smith")
    private String buyerName;
    @Schema(description = "바이어 직책", example = "Manager")
    private String buyerPosition;
    @Schema(description = "바이어 이메일", example = "john@example.com")
    private String buyerEmail;
    @Schema(description = "바이어 전화번호", example = "+1-234-567-8900")
    private String buyerTel;
    @Schema(description = "생성일시")
    private LocalDateTime createdAt;
    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    // 프론트 소비자가 buyerId 를 기대 — id 와 buyerId 둘 다 직렬화.
    public Integer getBuyerId() { return id; }

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
