package com.team2.master.query.controller;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.query.service.BuyerQueryService;
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

@Tag(name = "바이어 Query", description = "바이어 조회 API")
@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BuyerQueryController {

    private final BuyerQueryService buyerQueryService;

    @Operation(summary = "바이어 전체 조회", description = "페이징을 적용하여 바이어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/buyers")
    public ResponseEntity<PagedModel<EntityModel<BuyerResponse>>> getAllBuyers(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "1000") int size) {
        PagedResponse<BuyerResponse> result = buyerQueryService.getAllBuyersPaged(page, size);
        List<BuyerResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<BuyerResponse>> models = content.stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    @Operation(summary = "바이어 단건 조회", description = "ID로 특정 바이어를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "바이어를 찾을 수 없음")
    })
    @GetMapping("/api/buyers/{id}")
    public ResponseEntity<EntityModel<BuyerResponse>> getBuyer(@Parameter(description = "바이어 ID") @PathVariable("id") Integer id) {
        BuyerResponse response = buyerQueryService.getBuyer(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(id)).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers(0, 1000)).withRel("buyers")));
    }

    @Operation(summary = "거래처별 바이어 조회", description = "특정 거래처에 소속된 바이어 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/buyers/client/{clientId}")
    public ResponseEntity<PagedModel<EntityModel<BuyerResponse>>> getBuyersByClient(
            @Parameter(description = "거래처 ID") @PathVariable("clientId") Integer clientId,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "1000") int size) {
        PagedResponse<BuyerResponse> result = buyerQueryService.getBuyersByClientIdPaged(clientId, page, size);
        List<BuyerResponse> content = result.content() != null ? result.content() : List.of();
        List<EntityModel<BuyerResponse>> models = content.stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, result.totalElements());
        return ResponseEntity.ok(PagedModel.of(models, metadata));
    }

    @Operation(summary = "거래처 하위 바이어 조회", description = "거래처 리소스 하위 경로로 바이어 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<CollectionModel<EntityModel<BuyerResponse>>> getBuyersByClientNested(@Parameter(description = "거래처 ID") @PathVariable("clientId") Integer clientId) {
        List<EntityModel<BuyerResponse>> models = buyerQueryService.getBuyersByClientId(clientId).stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(BuyerQueryController.class).getBuyersByClientNested(clientId)).withSelfRel()));
    }
}
