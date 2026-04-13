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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "거래처 Query", description = "거래처 조회 API")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    @Operation(summary = "거래처 목록 조회", description = "검색 조건과 페이징을 적용하여 거래처 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ClientListResponse>>> getClients(
            @Parameter(description = "거래처명 검색") @RequestParam(name = "clientName", required = false) String clientName,
            @Parameter(description = "국가 ID 필터") @RequestParam(name = "countryId", required = false) Integer countryId,
            @Parameter(description = "거래처 상태 필터") @RequestParam(name = "clientStatus", required = false) String clientStatus,
            @Parameter(description = "부서 ID 필터") @RequestParam(name = "departmentId", required = false) Integer departmentId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "10") int size) {
        PagedResponse<ClientListResponse> result = clientQueryService.getClients(clientName, countryId, clientStatus, departmentId, page, size);
        List<EntityModel<ClientListResponse>> models = result.content().stream()
                .map(EntityModel::of).toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    @Operation(summary = "거래처 단건 조회", description = "ID로 특정 거래처를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "거래처를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClientResponse>> getClient(@Parameter(description = "거래처 ID") @PathVariable("id") Integer id) {
        ClientResponse response = clientQueryService.getClient(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients")));
    }

    @Operation(summary = "��체 거래처 목록 조회 (내부용)", description = "페이징 없이 전체 거래처 목록을 반환합니다. 서비스 간 ���신용.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/all")
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientQueryService.getAllClients());
    }

    @Operation(summary = "부서별 거래처 조회", description = "특정 부서에 배정된 거래처 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<CollectionModel<EntityModel<ClientResponse>>> getClientsByDepartment(@Parameter(description = "부서 ID") @PathVariable("departmentId") Integer departmentId) {
        List<EntityModel<ClientResponse>> models = clientQueryService.getClientsByDepartmentId(departmentId).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClientQueryController.class).getClient(c.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(ClientQueryController.class).getClientsByDepartment(departmentId)).withSelfRel()));
    }
}
