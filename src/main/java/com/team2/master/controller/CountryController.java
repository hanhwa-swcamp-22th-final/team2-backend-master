package com.team2.master.controller;

import com.team2.master.dto.CreateCountryRequest;
import com.team2.master.dto.UpdateCountryRequest;
import com.team2.master.entity.Country;
import com.team2.master.service.CountryCommandService;
import com.team2.master.service.CountryQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryCommandService countryCommandService;
    private final CountryQueryService countryQueryService;

    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        return ResponseEntity.ok(countryQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(countryQueryService.getById(id));
    }

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
