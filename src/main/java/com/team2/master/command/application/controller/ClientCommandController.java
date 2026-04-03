package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.command.application.dto.CreateClientRequest;
import com.team2.master.command.application.dto.UpdateClientRequest;
import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import com.team2.master.command.application.service.ClientCommandService;
import com.team2.master.query.controller.ClientQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientCommandController {

    private final ClientCommandService clientCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<ClientResponse>> createClient(@Valid @RequestBody CreateClientRequest request) {
        Client client = clientCommandService.createClient(request);
        ClientResponse response = ClientResponse.from(client);
        EntityModel<ClientResponse> model = EntityModel.of(response,
                linkTo(methodOn(ClientQueryController.class).getClient(client.getClientId())).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients"));
        URI location = linkTo(methodOn(ClientQueryController.class).getClient(client.getClientId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClientResponse>> updateClient(@PathVariable Integer id,
                                                       @Valid @RequestBody UpdateClientRequest request) {
        Client client = clientCommandService.updateClient(id, request);
        return ResponseEntity.ok(EntityModel.of(ClientResponse.from(client),
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients")));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EntityModel<ClientResponse>> changeStatus(@PathVariable Integer id,
                                                       @Valid @RequestBody ChangeStatusRequest request) {
        ClientStatus status = ClientStatus.valueOf(request.getStatus());
        Client client = clientCommandService.changeStatus(id, status);
        return ResponseEntity.ok(EntityModel.of(ClientResponse.from(client),
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel()));
    }
}
