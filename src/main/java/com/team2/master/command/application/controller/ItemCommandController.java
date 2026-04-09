package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.command.application.dto.CreateItemRequest;
import com.team2.master.command.application.dto.UpdateItemRequest;
import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.command.application.service.ItemCommandService;
import com.team2.master.query.controller.ItemQueryController;
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

@Tag(name = "품목 Command", description = "품목 등록/수정/상태변경 API")
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ItemCommandController {

    private final ItemCommandService itemCommandService;

    @Operation(summary = "품목 등록", description = "새로운 품목을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "품목 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Item>> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemCommandService.createItem(request);
        EntityModel<Item> model = EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(item.getItemId())).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items"));
        URI location = linkTo(methodOn(ItemQueryController.class).getItem(item.getItemId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "품목 수정", description = "기존 품목 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "품목 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "품목을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> updateItem(
            @Parameter(description = "품목 ID") @PathVariable("id") Integer id,
            @Valid @RequestBody UpdateItemRequest request) {
        Item item = itemCommandService.updateItem(id, request);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items")));
    }

    @Operation(summary = "품목 상태 변경", description = "품목의 활성/비활성 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상태값"),
            @ApiResponse(responseCode = "404", description = "품목을 찾을 수 없음")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<EntityModel<Item>> changeStatus(
            @Parameter(description = "품목 ID") @PathVariable("id") Integer id,
            @Valid @RequestBody ChangeStatusRequest request) {
        ItemStatus status = ItemStatus.valueOf(request.getStatus());
        Item item = itemCommandService.changeStatus(id, status);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel()));
    }
}
