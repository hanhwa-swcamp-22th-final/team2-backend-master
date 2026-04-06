package com.team2.master.query.controller;

import com.team2.master.query.dto.PortResponse;
import com.team2.master.query.service.PortQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "항구 Query", description = "항구 조회 API")
@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortQueryController {

    private final PortQueryService portQueryService;

    @Operation(summary = "항구 전체 조회", description = "등록된 모든 항구 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<PortResponse>> getAll() {
        return ResponseEntity.ok(portQueryService.getAll());
    }

    @Operation(summary = "항구 단건 조회", description = "ID로 특정 항구를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "항구를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PortResponse> getById(@Parameter(description = "항구 ID") @PathVariable Integer id) {
        return ResponseEntity.ok(portQueryService.getById(id));
    }
}
