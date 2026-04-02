package com.team2.master.query.controller;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
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
    public ResponseEntity<PagedResponse<ClientListResponse>> getClients(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) Integer countryId,
            @RequestParam(required = false) String clientStatus,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(clientQueryService.getClients(clientName, countryId, clientStatus, departmentId, page, size));
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
