package com.team2.master.controller;

import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.service.IncotermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
public class IncotermController {

    private final IncotermService incotermService;

    @GetMapping
    public ResponseEntity<List<Incoterm>> getAll() {
        return ResponseEntity.ok(incotermService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incoterm> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(incotermService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Incoterm> create(@Valid @RequestBody CreateIncotermRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incotermService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incoterm> update(@PathVariable Integer id, @Valid @RequestBody UpdateIncotermRequest request) {
        return ResponseEntity.ok(incotermService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        incotermService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
