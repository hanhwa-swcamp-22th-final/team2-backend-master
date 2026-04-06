package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "결제조건 수정 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentTermRequest {
    @Schema(description = "결제조건 코드", example = "TT30")
    private String paymentTermCode;
    @Schema(description = "결제조건명", example = "T/T 30 Days")
    private String paymentTermName;
    @Schema(description = "결제조건 설명", example = "선적 후 30일 이내 전신환 송금")
    private String paymentTermDescription;
}
