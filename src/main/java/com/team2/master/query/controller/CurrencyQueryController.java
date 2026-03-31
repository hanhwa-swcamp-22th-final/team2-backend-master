package com.team2.master.query.controller;

import com.team2.master.entity.Currency;
import com.team2.master.query.service.CurrencyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyQueryController {

    private final CurrencyQueryService currencyQueryService;

    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(currencyQueryService.getById(id));
    }
}
