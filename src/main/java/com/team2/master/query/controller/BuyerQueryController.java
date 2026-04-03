package com.team2.master.query.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.query.service.BuyerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class BuyerQueryController {

    private final BuyerQueryService buyerQueryService;

    @GetMapping("/api/buyers")
    public ResponseEntity<CollectionModel<EntityModel<BuyerResponse>>> getAllBuyers() {
        List<EntityModel<BuyerResponse>> models = buyerQueryService.getAllBuyers().stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withSelfRel()));
    }

    @GetMapping("/api/buyers/{id}")
    public ResponseEntity<EntityModel<BuyerResponse>> getBuyer(@PathVariable Integer id) {
        BuyerResponse response = buyerQueryService.getBuyer(id);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(id)).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers")));
    }

    @GetMapping("/api/buyers/client/{clientId}")
    public ResponseEntity<CollectionModel<EntityModel<BuyerResponse>>> getBuyersByClient(@PathVariable Integer clientId) {
        List<EntityModel<BuyerResponse>> models = buyerQueryService.getBuyersByClientId(clientId).stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(BuyerQueryController.class).getBuyersByClient(clientId)).withSelfRel()));
    }

    @GetMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<CollectionModel<EntityModel<BuyerResponse>>> getBuyersByClientNested(@PathVariable Integer clientId) {
        List<EntityModel<BuyerResponse>> models = buyerQueryService.getBuyersByClientId(clientId).stream()
                .map(b -> EntityModel.of(b,
                        linkTo(methodOn(BuyerQueryController.class).getBuyer(b.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(BuyerQueryController.class).getBuyersByClientNested(clientId)).withSelfRel()));
    }
}
