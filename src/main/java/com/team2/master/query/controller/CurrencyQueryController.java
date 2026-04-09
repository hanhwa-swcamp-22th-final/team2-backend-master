package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Currency;
import com.team2.master.query.service.CurrencyQueryService;
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

@Tag(name = "통화 Query", description = "통화 조회 API")
@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CurrencyQueryController {

    private final CurrencyQueryService currencyQueryService;

    @Operation(summary = "통화 전체 조회", description = "등록된 모든 통화 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Currency>>> getAll() {
        List<EntityModel<Currency>> models = currencyQueryService.getAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CurrencyQueryController.class).getById(c.getCurrencyId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "통화 단건 조회", description = "ID로 특정 통화를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Currency>> getById(@Parameter(description = "통화 ID") @PathVariable Integer id) {
        Currency currency = currencyQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies")));
    }
}
