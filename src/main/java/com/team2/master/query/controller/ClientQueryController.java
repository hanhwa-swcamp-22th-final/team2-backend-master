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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
            @Parameter(description = "팀 ID 필터") @RequestParam(name = "teamId", required = false) Integer teamId,
            @Parameter(description = "부서 ID 필터 (팀 경유)") @RequestParam(name = "departmentId", required = false) Integer departmentId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "10") int size) {
        // ADMIN 은 전체, 비ADMIN 은 같은 부서 거래처만.
        Integer effectiveTeamId = resolveEffectiveTeamId(teamId);
        Integer effectiveDepartmentId = resolveEffectiveDepartmentId(departmentId);
        PagedResponse<ClientListResponse> result = clientQueryService.getClients(clientName, countryId, clientStatus, effectiveTeamId, effectiveDepartmentId, page, size);
        List<ClientListResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<ClientListResponse>> models = content.stream()
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
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, null, 0, 10)).withRel("clients")));
    }

    @Operation(summary = "전체 거래처 목록 조회", description = "페이징을 적용하여 전체 거래처 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/all")
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getAllClients(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "1000") int size) {
        PagedResponse<ClientResponse> result = clientQueryService.getAllClientsPaged(page, size);
        List<ClientResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<ClientResponse>> models = content.stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClientQueryController.class).getClient(c.getId())).withSelfRel()))
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    @Operation(summary = "팀별 거래처 조회", description = "특정 팀에 배정된 거래처 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getClientsByTeam(
            @Parameter(description = "팀 ID") @PathVariable("teamId") Integer teamId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "1000") int size) {
        PagedResponse<ClientResponse> result = clientQueryService.getClientsByTeamIdPaged(teamId, page, size);
        List<ClientResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<ClientResponse>> models = content.stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClientQueryController.class).getClient(c.getId())).withSelfRel()))
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    @Operation(summary = "부서별 거래처 조회", description = "특정 부서에 소속된 팀들이 담당하는 거래처 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getClientsByDepartment(
            @Parameter(description = "부서 ID") @PathVariable("departmentId") Integer departmentId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "1000") int size) {
        PagedResponse<ClientResponse> result = clientQueryService.getClientsByDepartmentIdPaged(departmentId, page, size);
        List<ClientResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<ClientResponse>> models = content.stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClientQueryController.class).getClient(c.getId())).withSelfRel()))
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    /**
     * ADMIN: 명시 teamId 그대로 전달 (null 이면 전체).
     * SALES 등 비ADMIN: 같은 부서(departmentId) 소속 거래처 조회.
     *   → departmentId 기반 필터를 사용하도록 effectiveDepartmentId 반환.
     *   → teamId 는 null 로 설정하여 부서 내 전체 팀 거래처 포함.
     */
    private Integer resolveEffectiveTeamId(Integer requestedTeamId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return requestedTeamId;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) return requestedTeamId;
        // 비ADMIN: teamId 필터 대신 departmentId 필터를 사용하므로 teamId 는 null 반환
        return null;
    }

    /**
     * 비ADMIN 사용자의 departmentId 를 JWT 에서 추출.
     */
    private Integer resolveEffectiveDepartmentId(Integer requestedDepartmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return requestedDepartmentId;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        if (isAdmin) return requestedDepartmentId;
        if (auth.getPrincipal() instanceof Jwt jwt) {
            Integer deptId = jwt.getClaim("departmentId") != null
                    ? ((Number) jwt.getClaim("departmentId")).intValue()
                    : null;
            return deptId != null ? deptId : requestedDepartmentId;
        }
        return requestedDepartmentId;
    }
}
