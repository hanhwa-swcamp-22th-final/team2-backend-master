package com.team2.master.command.controller;

import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.ClientResponse;
import com.team2.master.dto.CreateClientRequest;
import com.team2.master.dto.UpdateClientRequest;
import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import com.team2.master.command.service.ClientCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientCommandController {

    private final ClientCommandService clientCommandService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientCommandService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.from(client));
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
}
