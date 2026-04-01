package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCountryRequest;
import com.team2.master.command.application.dto.UpdateCountryRequest;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.application.service.CountryCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryCommandController {

    private final CountryCommandService countryCommandService;

    @PostMapping
    public ResponseEntity<Country> create(@Valid @RequestBody CreateCountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(countryCommandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> update(@PathVariable Integer id, @Valid @RequestBody UpdateCountryRequest request) {
        return ResponseEntity.ok(countryCommandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        countryCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
