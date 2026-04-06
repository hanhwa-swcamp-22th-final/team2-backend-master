package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCurrencyRequest;
import com.team2.master.command.application.dto.UpdateCurrencyRequest;
import com.team2.master.command.domain.entity.Currency;
import com.team2.master.command.application.service.CurrencyCommandService;
import com.team2.master.query.controller.CurrencyQueryController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "통화 Command", description = "통화 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyCommandController {

    private final CurrencyCommandService currencyCommandService;

    @Operation(summary = "통화 등록", description = "새로운 통화를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "통화 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Currency>> create(@Valid @RequestBody CreateCurrencyRequest request) {
        Currency currency = currencyCommandService.create(request);
        EntityModel<Currency> model = EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(currency.getCurrencyId())).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies"));
        URI location = linkTo(methodOn(CurrencyQueryController.class).getById(currency.getCurrencyId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "통화 수정", description = "기존 통화 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "통화 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Currency>> update(@Parameter(description = "통화 ID") @PathVariable Integer id, @Valid @RequestBody UpdateCurrencyRequest request) {
        Currency currency = currencyCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies")));
    }

    @Operation(summary = "통화 삭제", description = "통화를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "통화 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "통화를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "통화 ID") @PathVariable Integer id) {
        currencyCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
