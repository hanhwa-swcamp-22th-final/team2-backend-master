package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePaymentTermRequest;
import com.team2.master.command.application.dto.UpdatePaymentTermRequest;
import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.command.application.service.PaymentTermCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제조건 Command", description = "결제조건 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermCommandController {

    private final PaymentTermCommandService paymentTermCommandService;

    @Operation(summary = "결제조건 등록", description = "새로운 결제조건을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "결제조건 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<PaymentTerm> create(@Valid @RequestBody CreatePaymentTermRequest request) {
        PaymentTerm paymentTerm = paymentTermCommandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentTerm);
    }

    @Operation(summary = "결제조건 수정", description = "기존 결제조건 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제조건 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "결제조건을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTerm> update(@Parameter(description = "결제조건 ID") @PathVariable Integer id, @Valid @RequestBody UpdatePaymentTermRequest request) {
        PaymentTerm paymentTerm = paymentTermCommandService.update(id, request);
        return ResponseEntity.ok(paymentTerm);
    }

    @Operation(summary = "결제조건 삭제", description = "결제조건을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "결제조건 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "결제조건을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "결제조건 ID") @PathVariable Integer id) {
        paymentTermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
