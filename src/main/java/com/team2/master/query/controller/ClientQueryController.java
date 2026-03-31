package com.team2.master.query.controller;

import com.team2.master.dto.ClientResponse;
import com.team2.master.query.service.ClientQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientQueryController {

    private final ClientQueryService clientQueryService;

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientQueryService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(clientQueryService.getClient(id));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(clientQueryService.getClientsByDepartmentId(departmentId));
    }
}
