package com.team2.master.controller;

import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.ClientResponse;
import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.service.ClientCommandService;
import com.team2.master.service.ClientQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientCommandService clientCommandService;
    private final ClientQueryService clientQueryService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientCommandService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.from(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientQueryService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(clientQueryService.getClient(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Integer id,
                                                       @Valid @RequestBody UpdateClientRequest request) {
        Client client = clientCommandService.updateClient(id, request);
        return ResponseEntity.ok(ClientResponse.from(client));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientResponse> changeStatus(@PathVariable Integer id,
                                                       @Valid @RequestBody ChangeStatusRequest request) {
        ClientStatus status = ClientStatus.valueOf(request.getStatus());
        Client client = clientCommandService.changeStatus(id, status);
        return ResponseEntity.ok(ClientResponse.from(client));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDepartment(@PathVariable Integer departmentId) {
        return ResponseEntity.ok(clientQueryService.getClientsByDepartmentId(departmentId));
    }
}
