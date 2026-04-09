package com.team2.master.query.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.query.service.BuyerQueryService;
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

import java.util.List;

/**
 * 서비스 간 내부 호출 전용 바이어 조회 API.
 * 인증: X-Internal-Token 헤더 (InternalApiTokenFilter 가 검증).
 * Gateway 에서 /api/**\/internal/** 경로를 denyAll 로 외부 차단한다.
 *
 * 현재 호출자: Documents 서비스 (AutoEmailRecipientResolver — 메일 수신자 자동 해소)
 */
@Tag(name = "바이어 내부 조회", description = "서비스 간 시스템 호출 전용. X-Internal-Token 필요")
@RestController
@RequestMapping("/api/buyers/internal")
@RequiredArgsConstructor
public class InternalBuyerQueryController {

    private final BuyerQueryService buyerQueryService;

    @Operation(
            summary = "거래처 기준 바이어 목록 조회 (내부 전용)",
            description = "거래처에 소속된 바이어 목록을 플랫 List 로 반환. HATEOAS wrapper 없음."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "X-Internal-Token 누락/불일치")
    })
    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClient(
            @Parameter(description = "거래처 ID", required = true)
            @PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }
}
