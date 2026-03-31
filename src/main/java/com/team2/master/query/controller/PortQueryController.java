package com.team2.master.query.controller;

import com.team2.master.dto.PortResponse;
import com.team2.master.query.service.PortQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortQueryController {

    private final PortQueryService portQueryService;

    @GetMapping
    public ResponseEntity<List<PortResponse>> getAll() {
        return ResponseEntity.ok(portQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(portQueryService.getById(id));
    }
}
