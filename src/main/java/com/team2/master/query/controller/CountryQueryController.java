package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Country;
import com.team2.master.query.service.CountryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "국가 Query", description = "국가 조회 API")
@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryQueryController {

    private final CountryQueryService countryQueryService;

    @Operation(summary = "국가 전체 조회", description = "등록된 모든 국가 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Country>>> getAll() {
        List<EntityModel<Country>> models = countryQueryService.getAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CountryQueryController.class).getById(c.getCountryId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(CountryQueryController.class).getAll()).withSelfRel()));
    }

    @Operation(summary = "국가 단건 조회", description = "ID로 특정 국가를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "국가를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Country>> getById(@Parameter(description = "국가 ID") @PathVariable Integer id) {
        Country country = countryQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries")));
    }
}
