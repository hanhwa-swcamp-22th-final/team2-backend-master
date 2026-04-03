package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCountryRequest;
import com.team2.master.command.application.dto.UpdateCountryRequest;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.application.service.CountryCommandService;
import com.team2.master.query.controller.CountryQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryCommandController {

    private final CountryCommandService countryCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<Country>> create(@Valid @RequestBody CreateCountryRequest request) {
        Country country = countryCommandService.create(request);
        EntityModel<Country> model = EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(country.getCountryId())).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries"));
        URI location = linkTo(methodOn(CountryQueryController.class).getById(country.getCountryId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Country>> update(@PathVariable Integer id, @Valid @RequestBody UpdateCountryRequest request) {
        Country country = countryCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(country,
                linkTo(methodOn(CountryQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CountryQueryController.class).getAll()).withRel("countries")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        countryCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
