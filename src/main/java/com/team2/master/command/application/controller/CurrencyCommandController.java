package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCurrencyRequest;
import com.team2.master.command.application.dto.UpdateCurrencyRequest;
import com.team2.master.command.domain.entity.Currency;
import com.team2.master.command.application.service.CurrencyCommandService;
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

@Tag(name = "통화 Command", description = "통화 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyCommandController {

    private final CurrencyCommandService currencyCommandService;

    @Operation(summary = "통화 등록", description = "새로운 통화를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "통화 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<Currency> create(@Valid @RequestBody CreateCurrencyRequest request) {
        Currency currency = currencyCommandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(currency);
    }

    @Operation(summary = "통화 수정", description = "기존 통화 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "통화 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@Parameter(description = "통화 ID") @PathVariable Integer id, @Valid @RequestBody UpdateCurrencyRequest request) {
        Currency currency = currencyCommandService.update(id, request);
        return ResponseEntity.ok(currency);
    }

    @Operation(summary = "통화 삭제", description = "통화를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "통화 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "통화 ID") @PathVariable Integer id) {
        currencyCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
