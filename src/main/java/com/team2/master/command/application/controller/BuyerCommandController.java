package com.team2.master.command.application.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.application.service.BuyerCommandService;
import com.team2.master.query.controller.BuyerQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class BuyerCommandController {

    private final BuyerCommandService buyerCommandService;

    @PostMapping("/api/buyers")
    public ResponseEntity<EntityModel<BuyerResponse>> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerCommandService.createBuyer(request);
        BuyerResponse response = BuyerResponse.from(buyer);
        EntityModel<BuyerResponse> model = EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers"));
        URI location = linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PostMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<EntityModel<BuyerResponse>> createBuyerNested(@PathVariable Integer clientId,
                                                           @Valid @RequestBody CreateBuyerRequest request) {
        request.setClientId(clientId);
        Buyer buyer = buyerCommandService.createBuyer(request);
        BuyerResponse response = BuyerResponse.from(buyer);
        EntityModel<BuyerResponse> model = EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers"));
        URI location = linkTo(methodOn(BuyerQueryController.class).getBuyer(buyer.getBuyerId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/api/buyers/{id}")
    public ResponseEntity<EntityModel<BuyerResponse>> updateBuyer(@PathVariable Integer id,
                                                     @Valid @RequestBody UpdateBuyerRequest request) {
        Buyer buyer = buyerCommandService.updateBuyer(id, request);
        BuyerResponse response = BuyerResponse.from(buyer);
        return ResponseEntity.ok(EntityModel.of(response,
                linkTo(methodOn(BuyerQueryController.class).getBuyer(id)).withSelfRel(),
                linkTo(methodOn(BuyerQueryController.class).getAllBuyers()).withRel("buyers")));
    }

    @DeleteMapping("/api/buyers/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Integer id) {
        buyerCommandService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
