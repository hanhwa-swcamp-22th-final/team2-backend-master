package com.team2.master.query.controller;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.query.service.ClientQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "거래처 Query", description = "거래처 조회 API")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    @Operation(summary = "거래처 목록 조회", description = "검색 조건과 페이징을 적용하여 거래처 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<PagedResponse<ClientListResponse>> getClients(
            @Parameter(description = "거래처명 검색") @RequestParam(required = false) String clientName,
            @Parameter(description = "국가 ID 필터") @RequestParam(required = false) Integer countryId,
            @Parameter(description = "거래처 상태 필터") @RequestParam(required = false) String clientStatus,
            @Parameter(description = "부서 ID 필터") @RequestParam(required = false) Integer departmentId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(clientQueryService.getClients(clientName, countryId, clientStatus, departmentId, page, size));
    }

    @Operation(summary = "거래처 단건 조회", description = "ID로 특정 거래처를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "거래처를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@Parameter(description = "거래처 ID") @PathVariable Integer id) {
        return ResponseEntity.ok(clientQueryService.getClient(id));
    }

    @Operation(summary = "전체 거래처 목록 조회 (내부용)", description = "페이징 없이 전체 거래처 목록을 반환합니다. 서비스 간 통신용.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/all")
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientQueryService.getAllClients());
    }

    @Operation(summary = "부서별 거래처 조회", description = "특정 부서에 배정된 거래처 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDepartment(@Parameter(description = "부서 ID") @PathVariable Integer departmentId) {
        return ResponseEntity.ok(clientQueryService.getClientsByDepartmentId(departmentId));
    }
}
