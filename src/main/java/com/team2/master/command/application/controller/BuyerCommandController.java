package com.team2.master.command.application.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.application.service.BuyerCommandService;
import com.team2.master.query.controller.BuyerQueryController;
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

@Tag(name = "바이어 Command", description = "바이어 등록/수정/삭제 API")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BuyerCommandController {

    private final BuyerCommandService buyerCommandService;

    @Operation(summary = "바이어 등록", description = "새로운 바이어를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "바이어 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/api/buyers")
    public ResponseEntity<EntityModel<BuyerResponse>> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerCommandService.createBuyer(request);
        BuyerResponse response = BuyerResponse.from(buyer);
        EntityModel<BuyerResponse> model = EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers"));
        URI location = linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "거래처 하위 바이어 등록", description = "특정 거래처에 소속된 바이어를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "바이어 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<EntityModel<BuyerResponse>> createBuyerNested(
            @Parameter(description = "거래처 ID") @PathVariable Integer clientId,
            @Valid @RequestBody CreateBuyerRequest request) {
        request.setClientId(clientId);
        Buyer buyer = buyerCommandService.createBuyer(request);
        BuyerResponse response = BuyerResponse.from(buyer);
        EntityModel<BuyerResponse> model = EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers"));
        URI location = linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "바이어 수정", description = "기존 바이어 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "바이어 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "바이어를 찾을 수 없음")
    })
    @PutMapping("/api/buyers/{id}")
    public ResponseEntity<EntityModel<BuyerResponse>> updateBuyer(
            @Parameter(description = "바이어 ID") @PathVariable Integer id,
            @Valid @RequestBody UpdateBuyerRequest request) {
        Buyer buyer = buyerCommandService.updateBuyer(id, request);
        BuyerResponse response = BuyerResponse.from(buyer);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(id)).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers")));
    }

    @Operation(summary = "바이어 삭제", description = "바이어를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "바이어 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "바이어를 찾을 수 없음")
    })
    @DeleteMapping("/api/buyers/{id}")
    public ResponseEntity<Void> deleteBuyer(@Parameter(description = "바이어 ID") @PathVariable Integer id) {
        buyerCommandService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
