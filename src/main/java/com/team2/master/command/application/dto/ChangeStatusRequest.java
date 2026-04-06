package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "상태 변경 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusRequest {
    @Schema(description = "변경할 상태값", example = "ACTIVE")
    @NotBlank(message = "상태값은 필수입니다.")
    private String status;
}
