package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateIncotermRequest;
import com.team2.master.command.application.dto.UpdateIncotermRequest;
import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.command.application.service.IncotermCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
public class IncotermCommandController {

    private final IncotermCommandService incotermCommandService;

    @PostMapping
    public ResponseEntity<Incoterm> create(@Valid @RequestBody CreateIncotermRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incotermCommandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incoterm> update(@PathVariable Integer id, @Valid @RequestBody UpdateIncotermRequest request) {
        return ResponseEntity.ok(incotermCommandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        incotermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
