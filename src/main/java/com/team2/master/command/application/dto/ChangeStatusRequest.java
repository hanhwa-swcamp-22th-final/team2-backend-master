package com.team2.master.command.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusRequest {
    @NotBlank(message = "상태값은 필수입니다.")
    private String status;
}
