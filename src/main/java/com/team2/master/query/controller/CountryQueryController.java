package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Country;
import com.team2.master.query.service.CountryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryQueryController {

    private final CountryQueryService countryQueryService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Country>>> getAll() {
        List<EntityModel<Country>> models = countryQueryService.getAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CountryQueryController.class).getById(c.getCountryId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(CountryQueryController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Country>> getById(@PathVariable Integer id) {
        Country country = countryQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries")));
    }
}
