package com.team2.master.controller;

import com.team2.master.dto.CreateCurrencyRequest;
import com.team2.master.dto.UpdateCurrencyRequest;
import com.team2.master.entity.Currency;
import com.team2.master.service.CurrencyCommandService;
import com.team2.master.service.CurrencyQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyCommandService currencyCommandService;
    private final CurrencyQueryService currencyQueryService;

    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(currencyQueryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Currency> create(@Valid @RequestBody CreateCurrencyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyCommandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@PathVariable Integer id, @Valid @RequestBody UpdateCurrencyRequest request) {
        return ResponseEntity.ok(currencyCommandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        currencyCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
