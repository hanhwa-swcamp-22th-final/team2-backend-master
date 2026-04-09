package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateIncotermRequest;
import com.team2.master.command.application.dto.UpdateIncotermRequest;
import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.command.application.service.IncotermCommandService;
import com.team2.master.query.controller.IncotermQueryController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "인코텀 Command", description = "인코텀 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class IncotermCommandController {

    private final IncotermCommandService incotermCommandService;

    @Operation(summary = "인코텀 등록", description = "새로운 인코텀을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "인코텀 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Incoterm>> create(@Valid @RequestBody CreateIncotermRequest request) {
        Incoterm incoterm = incotermCommandService.create(request);
        EntityModel<Incoterm> model = EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(incoterm.getIncotermId())).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms"));
        URI location = linkTo(methodOn(IncotermQueryController.class).getById(incoterm.getIncotermId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "인코텀 수정", description = "기존 인코텀 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인코텀 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "인코텀을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Incoterm>> update(@Parameter(description = "인코텀 ID") @PathVariable("id") Integer id, @Valid @RequestBody UpdateIncotermRequest request) {
        Incoterm incoterm = incotermCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms")));
    }

    @Operation(summary = "인코텀 삭제", description = "인코텀을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "인코텀 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "인코텀을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "인코텀 ID") @PathVariable("id") Integer id) {
        incotermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
