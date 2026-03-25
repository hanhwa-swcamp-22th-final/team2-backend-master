package com.team2.master.controller;

import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.service.BuyerService;
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
    public ResponseEntity<Buyer> createBuyer(@RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(buyer);
    }

    @GetMapping
    public ResponseEntity<List<Buyer>> getAllBuyers() {
        return ResponseEntity.ok(buyerService.getAllBuyers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> getBuyer(@PathVariable Integer id) {
        return ResponseEntity.ok(buyerService.getBuyer(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Buyer>> getBuyersByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerService.getBuyersByClientId(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Integer id,
                                             @RequestBody UpdateBuyerRequest request) {
        return ResponseEntity.ok(buyerService.updateBuyer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Integer id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
