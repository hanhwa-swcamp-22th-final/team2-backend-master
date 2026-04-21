package com.team2.master.query.controller;

import com.team2.master.query.dto.ClientResponse;
import com.team2.master.query.service.ClientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서비스 간 내부 호출 전용 거래처 조회 API.
 * 인증: X-Internal-Token 헤더 (InternalApiTokenFilter 가 검증).
 * Gateway 에서 /api/clients/internal/** 경로를 denyAll 로 외부 차단.
 *
 * 호출자:
 *   - Documents.PurchaseOrderCreationService (PO 생성 시 paymentTerm/port 스냅샷)
 */
@Tag(name = "거래처 내부 조회", description = "서비스 간 시스템 호출 전용. X-Internal-Token 필요")
@RestController
@RequestMapping("/api/clients/internal")
@RequiredArgsConstructor
public class InternalClientQueryController {

    private final ClientQueryService clientQueryService;

    @Operation(
            summary = "거래처 단건 조회 (내부 전용)",
            description = "clientId 로 거래처 상세 조회. paymentTermName / portName / currencyName 등 enrich 포함."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "X-Internal-Token 누락/불일치"),
            @ApiResponse(responseCode = "404", description = "거래처를 찾을 수 없음")
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponse> getClientById(
            @Parameter(description = "거래처 ID", required = true)
            @PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(clientQueryService.getClient(clientId));
    }
}
