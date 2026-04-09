package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.query.service.PaymentTermQueryService;
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

@Tag(name = "결제조건 Query", description = "결제조건 조회 API")
@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PaymentTermQueryController {

    private final PaymentTermQueryService paymentTermQueryService;

    @Operation(summary = "결제조건 전체 조회", description = "등록된 모든 결제조건 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PaymentTerm>>> getAll() {
        List<EntityModel<PaymentTerm>> models = paymentTermQueryService.getAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PaymentTermQueryController.class).getById(p.getPaymentTermId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "결제조건 단건 조회", description = "ID로 특정 결제조건을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "결제조건을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PaymentTerm>> getById(@Parameter(description = "결제조건 ID") @PathVariable("id") Integer id) {
        PaymentTerm paymentTerm = paymentTermQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(paymentTerm,
                linkTo(methodOn(PaymentTermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withRel("payment-terms")));
    }
}
