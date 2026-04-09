package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePortRequest;
import com.team2.master.query.dto.PortResponse;
import com.team2.master.command.application.dto.UpdatePortRequest;
import com.team2.master.command.application.service.PortCommandService;
import com.team2.master.query.controller.PortQueryController;
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

@Tag(name = "항구 Command", description = "항구 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PortCommandController {

    private final PortCommandService portCommandService;

    @Operation(summary = "항구 등록", description = "새로운 항구를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "항구 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<PortResponse>> create(@Valid @RequestBody CreatePortRequest request) {
        PortResponse port = portCommandService.create(request);
        EntityModel<PortResponse> model = EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(port.getId())).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports"));
        URI location = linkTo(methodOn(PortQueryController.class).getById(port.getId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "항구 수정", description = "기존 항구 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "항구 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "항구를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PortResponse>> update(@Parameter(description = "항구 ID") @PathVariable("id") Integer id, @Valid @RequestBody UpdatePortRequest request) {
        PortResponse port = portCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports")));
    }

    @Operation(summary = "항구 삭제", description = "항구를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "항구 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "항구를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "항구 ID") @PathVariable("id") Integer id) {
        portCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
