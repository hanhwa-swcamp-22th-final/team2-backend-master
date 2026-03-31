package com.team2.master.controller;

import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.ClientResponse;
import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.service.ClientService;
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

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.from(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> responses = clientService.getAllClients().stream()
                .map(ClientResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(ClientResponse.from(clientService.getClient(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Integer id,
                                                       @Valid @RequestBody UpdateClientRequest request) {
        return ResponseEntity.ok(ClientResponse.from(clientService.updateClient(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientResponse> changeStatus(@PathVariable Integer id,
                                                       @Valid @RequestBody ChangeStatusRequest request) {
        ClientStatus status = ClientStatus.valueOf(request.getStatus());
        return ResponseEntity.ok(ClientResponse.from(clientService.changeStatus(id, status)));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ClientResponse>> getClientsByDepartment(@PathVariable Integer departmentId) {
        List<ClientResponse> responses = clientService.getClientsByDepartmentId(departmentId).stream()
                .map(ClientResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
