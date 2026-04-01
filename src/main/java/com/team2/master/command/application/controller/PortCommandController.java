package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePortRequest;
import com.team2.master.query.dto.PortResponse;
import com.team2.master.command.application.dto.UpdatePortRequest;
import com.team2.master.command.application.service.PortCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortCommandController {

    private final PortCommandService portCommandService;

    @PostMapping
    public ResponseEntity<PortResponse> create(@Valid @RequestBody CreatePortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portCommandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortResponse> update(@PathVariable Integer id, @Valid @RequestBody UpdatePortRequest request) {
        return ResponseEntity.ok(portCommandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
