package com.team2.master.controller;

import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Port;
import com.team2.master.service.PortService;
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
    public ResponseEntity<List<Port>> getAll() {
        return ResponseEntity.ok(portService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Port> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(portService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Port> create(@RequestBody CreatePortRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Port> update(@PathVariable Integer id, @RequestBody UpdatePortRequest request) {
        return ResponseEntity.ok(portService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portService.delete(id);
        return ResponseEntity.ok().build();
    }
}
