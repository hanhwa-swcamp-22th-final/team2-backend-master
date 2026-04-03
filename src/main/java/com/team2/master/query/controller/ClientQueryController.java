package com.team2.master.query.controller;

import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
import com.team2.master.query.service.ClientQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public ResponseEntity<EntityModel<ClientResponse>> getClient(@PathVariable Integer id) {
        ClientResponse response = clientQueryService.getClient(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(ClientQueryController.class).getClient(id)).withSelfRel(),
                linkTo(methodOn(ClientQueryController.class).getClients(null, null, null, null, 0, 10)).withRel("clients")));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<CollectionModel<EntityModel<ClientResponse>>> getClientsByDepartment(@PathVariable Integer departmentId) {
        List<EntityModel<ClientResponse>> models = clientQueryService.getClientsByDepartmentId(departmentId).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClientQueryController.class).getClient(c.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(ClientQueryController.class).getClientsByDepartment(departmentId)).withSelfRel()));
    }
}
