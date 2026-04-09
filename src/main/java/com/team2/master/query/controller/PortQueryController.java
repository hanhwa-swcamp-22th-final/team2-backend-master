package com.team2.master.query.controller;

import com.team2.master.query.dto.PortResponse;
import com.team2.master.query.service.PortQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "항구 Query", description = "항구 조회 API")
@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PortQueryController {

    private final PortQueryService portQueryService;

    @Operation(summary = "항구 전체 조회", description = "등록된 모든 항구 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PortResponse>>> getAll() {
        List<EntityModel<PortResponse>> models = portQueryService.getAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PortQueryController.class).getById(p.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PortQueryController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "항구 단건 조회", description = "ID로 특정 항구를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "항구를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PortResponse>> getById(@Parameter(description = "항구 ID") @PathVariable Integer id) {
        PortResponse port = portQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports")));
    }
}
