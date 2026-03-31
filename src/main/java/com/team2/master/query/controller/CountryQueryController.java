package com.team2.master.query.controller;

import com.team2.master.entity.Country;
import com.team2.master.query.service.CountryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryQueryController {

    private final CountryQueryService countryQueryService;

    @GetMapping
    public ResponseEntity<List<Country>> getAll() {
        return ResponseEntity.ok(countryQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(countryQueryService.getById(id));
    }
}
