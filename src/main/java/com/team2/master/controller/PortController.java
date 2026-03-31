package com.team2.master.controller;

import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.PortResponse;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Port;
import com.team2.master.service.PortService;
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

    private final PortService portService;

    @GetMapping
    public ResponseEntity<List<PortResponse>> getAll() {
        List<PortResponse> responses = portService.getAll().stream()
                .map(PortResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(PortResponse.from(portService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<PortResponse> create(@Valid @RequestBody CreatePortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PortResponse.from(portService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdatePortRequest request) {
        return ResponseEntity.ok(PortResponse.from(portService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
