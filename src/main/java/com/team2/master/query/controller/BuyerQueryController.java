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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "바이어 Query", description = "바이어 조회 API")
@RestController
@RequiredArgsConstructor
public class BuyerQueryController {

    private final BuyerQueryService buyerQueryService;

    @Operation(summary = "바이어 전체 조회", description = "등록된 모든 바이어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/buyers")
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        return ResponseEntity.ok(buyerQueryService.getAllBuyers());
    }

    @Operation(summary = "바이어 단건 조회", description = "ID로 특정 바이어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "바이어를 찾을 수 없음")
    })
    @GetMapping("/api/buyers/{id}")
    public ResponseEntity<BuyerResponse> getBuyer(@Parameter(description = "바이어 ID") @PathVariable Integer id) {
        return ResponseEntity.ok(buyerQueryService.getBuyer(id));
    }

    @Operation(summary = "거래처별 바이어 조회", description = "특정 거래처에 소속된 바이어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/buyers/client/{clientId}")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClient(@Parameter(description = "거래처 ID") @PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }

    @Operation(summary = "거래처 하위 바이어 조회", description = "거래처 리소스 하위 경로로 바이어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClientNested(@Parameter(description = "거래처 ID") @PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }
}
