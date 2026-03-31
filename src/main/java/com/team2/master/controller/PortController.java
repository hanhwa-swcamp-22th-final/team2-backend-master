package com.team2.master.controller;

import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.PortResponse;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.service.PortCommandService;
import com.team2.master.service.PortQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortController {

    private final PortCommandService portCommandService;
    private final PortQueryService portQueryService;

    @GetMapping
    public ResponseEntity<List<PortResponse>> getAll() {
        return ResponseEntity.ok(portQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(portQueryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PortResponse> create(@Valid @RequestBody CreatePortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PortResponse.from(portCommandService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdatePortRequest request) {
        return ResponseEntity.ok(PortResponse.from(portCommandService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
