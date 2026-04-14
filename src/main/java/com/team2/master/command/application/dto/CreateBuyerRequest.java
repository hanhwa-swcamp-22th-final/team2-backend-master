package com.team2.master.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "거래처 ID는 필수입니다.")
    @Schema(description = "거래처 ID", example = "1")
    private Integer clientId;

    @NotBlank(message = "바이어명은 필수입니다.")
    @Size(max = 100)
    @Schema(description = "바이어명", example = "John Smith")
    private String buyerName;

    @Size(max = 100)
    @Schema(description = "바이어 직책 (팀장/팀원)", example = "팀원")
    private String buyerPosition;

    @NotBlank(message = "바이어 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    @Size(max = 255)
    @Schema(description = "바이어 이메일", example = "john@example.com")
    private String buyerEmail;

    @Pattern(regexp = "^$|^\\+?[\\d\\s()-]{7,20}$", message = "올바른 전화번호 형식을 입력하세요.")
    @Size(max = 50)
    @Schema(description = "바이어 전화번호", example = "+1-234-567-8900")
    private String buyerTel;
}
