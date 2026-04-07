package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCountryRequest;
import com.team2.master.command.application.dto.UpdateCountryRequest;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.application.service.CountryCommandService;
import com.team2.master.query.controller.CountryQueryController;
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

@Tag(name = "국가 Command", description = "국가 등록/수정/삭제 API")
@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryCommandController {

    private final CountryCommandService countryCommandService;

    @Operation(summary = "국가 등록", description = "새로운 국가를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "국가 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Country>> create(@Valid @RequestBody CreateCountryRequest request) {
        Country country = countryCommandService.create(request);
        EntityModel<Country> model = EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(country.getCountryId())).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries"));
        URI location = linkTo(methodOn(CountryQueryController.class).getById(country.getCountryId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Operation(summary = "국가 수정", description = "기존 국가 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "국가 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "국가를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Country>> update(@Parameter(description = "국가 ID") @PathVariable Integer id, @Valid @RequestBody UpdateCountryRequest request) {
        Country country = countryCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries")));
    }

    @Operation(summary = "국가 삭제", description = "국가를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "국가 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "국가를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "국가 ID") @PathVariable Integer id) {
        countryCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
