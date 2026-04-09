package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ItemListResponse;
import com.team2.master.query.service.ItemQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "품목 Query", description = "품목 조회 API")
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @Operation(summary = "품목 목록 조회", description = "검색 조건과 페이징을 적용하여 품목 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<PagedResponse<ItemListResponse>> getItems(
            @Parameter(description = "품목명 검색") @RequestParam(name = "itemName", required = false) String itemName,
            @Parameter(description = "품목 카테고리 필터") @RequestParam(name = "itemCategory", required = false) String itemCategory,
            @Parameter(description = "품목 상태 필터") @RequestParam(name = "itemStatus", required = false) String itemStatus,
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(itemQueryService.getItems(itemName, itemCategory, itemStatus, page, size));
    }

    @Operation(summary = "품목 단건 조회", description = "ID로 특정 품목을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "품목을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> getItem(@Parameter(description = "품목 ID") @PathVariable("id") Integer id) {
        Item item = itemQueryService.getItem(id);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items")));
    }
}
