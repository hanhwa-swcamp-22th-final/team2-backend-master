package com.team2.master.controller;

import com.team2.master.dto.BuyerResponse;
import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping
    public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }

    @GetMapping
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        List<BuyerResponse> responses = buyerService.getAllBuyers().stream()
                .map(BuyerResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerResponse> getBuyer(@PathVariable Integer id) {
        return ResponseEntity.ok(BuyerResponse.from(buyerService.getBuyer(id)));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClient(@PathVariable Integer clientId) {
        List<BuyerResponse> responses = buyerService.getBuyersByClientId(clientId).stream()
                .map(BuyerResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuyerResponse> updateBuyer(@PathVariable Integer id,
                                                     @Valid @RequestBody UpdateBuyerRequest request) {
        return ResponseEntity.ok(BuyerResponse.from(buyerService.updateBuyer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Integer id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
