package com.team2.master.command.application.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.command.application.dto.CreateBuyerRequest;
import com.team2.master.command.application.dto.UpdateBuyerRequest;
import com.team2.master.command.domain.entity.Buyer;
import com.team2.master.command.application.service.BuyerCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BuyerCommandController {

    private final BuyerCommandService buyerCommandService;

    @PostMapping("/api/buyers")
    public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerCommandService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }

    @PostMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<BuyerResponse> createBuyerNested(@PathVariable Integer clientId,
                                                           @Valid @RequestBody CreateBuyerRequest request) {
        request.setClientId(clientId);
        Buyer buyer = buyerCommandService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }

    @PutMapping("/api/buyers/{id}")
    public ResponseEntity<BuyerResponse> updateBuyer(@PathVariable Integer id,
                                                     @Valid @RequestBody UpdateBuyerRequest request) {
        Buyer buyer = buyerCommandService.updateBuyer(id, request);
        return ResponseEntity.ok(BuyerResponse.from(buyer));
    }

    @DeleteMapping("/api/buyers/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Integer id) {
        buyerCommandService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
