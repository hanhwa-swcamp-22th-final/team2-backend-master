package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Currency;
import com.team2.master.query.service.CurrencyQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "통화 Query", description = "통화 조회 API")
@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyQueryController {

    private final CurrencyQueryService currencyQueryService;

    @Operation(summary = "통화 전체 조회", description = "등록된 모든 통화 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyQueryService.getAll());
    }

    @Operation(summary = "통화 단건 조회", description = "ID로 특정 통화를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@Parameter(description = "통화 ID") @PathVariable Integer id) {
        return ResponseEntity.ok(currencyQueryService.getById(id));
    }
}
