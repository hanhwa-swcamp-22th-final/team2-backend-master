package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "바이어 등록 요청")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBuyerRequest {
    @Schema(description = "거래처 ID", example = "1")
    private Integer clientId;
    @Schema(description = "바이어명", example = "John Smith")
    private String buyerName;
    @Schema(description = "바이어 직책", example = "Manager")
    private String buyerPosition;
    @Schema(description = "바이어 이메일", example = "john@example.com")
    private String buyerEmail;
    @Schema(description = "바이어 전화번호", example = "+1-234-567-8900")
    private String buyerTel;
}
