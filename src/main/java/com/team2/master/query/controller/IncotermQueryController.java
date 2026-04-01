package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.query.service.IncotermQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
public class IncotermQueryController {

    private final IncotermQueryService incotermQueryService;

    @GetMapping
    public ResponseEntity<List<Incoterm>> getAll() {
        return ResponseEntity.ok(incotermQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incoterm> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(incotermQueryService.getById(id));
    }
}
