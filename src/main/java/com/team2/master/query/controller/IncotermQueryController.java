package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.query.service.IncotermQueryService;
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

@Tag(name = "인코텀 Query", description = "인코텀 조회 API")
@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class IncotermQueryController {

    private final IncotermQueryService incotermQueryService;

    @Operation(summary = "인코텀 전체 조회", description = "등록된 모든 인코텀 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Incoterm>>> getAll() {
        List<EntityModel<Incoterm>> models = incotermQueryService.getAll().stream()
                .map(i -> EntityModel.of(i,
                        linkTo(methodOn(IncotermQueryController.class).getById(i.getIncotermId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(IncotermQueryController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "인코텀 단건 조회", description = "ID로 특정 인코텀을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "인코텀을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Incoterm>> getById(@Parameter(description = "인코텀 ID") @PathVariable("id") Integer id) {
        Incoterm incoterm = incotermQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms")));
    }
}
