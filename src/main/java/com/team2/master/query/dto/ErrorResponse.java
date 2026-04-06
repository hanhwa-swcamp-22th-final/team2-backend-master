package com.team2.master.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "에러 응답")
@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "에러 메시지", example = "리소스를 찾을 수 없습니다.")
    private String message;
}
