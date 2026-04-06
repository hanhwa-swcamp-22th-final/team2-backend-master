package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.query.service.PaymentTermQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "결제조건 Query", description = "결제조건 조회 API")
@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermQueryController {

    private final PaymentTermQueryService paymentTermQueryService;

    @Operation(summary = "결제조건 전체 조회", description = "등록된 모든 결제조건 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<PaymentTerm>> getAll() {
        return ResponseEntity.ok(paymentTermQueryService.getAll());
    }

    @Operation(summary = "결제조건 단건 조회", description = "ID로 특정 결제조건을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "결제조건을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTerm> getById(@Parameter(description = "결제조건 ID") @PathVariable Integer id) {
        return ResponseEntity.ok(paymentTermQueryService.getById(id));
    }
}
