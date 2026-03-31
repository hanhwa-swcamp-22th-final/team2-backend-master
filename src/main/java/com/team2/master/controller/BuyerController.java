package com.team2.master.controller;

import com.team2.master.dto.BuyerResponse;
import com.team2.master.dto.CreateBuyerRequest;
import com.team2.master.dto.UpdateBuyerRequest;
import com.team2.master.entity.Buyer;
import com.team2.master.service.BuyerCommandService;
import com.team2.master.service.BuyerQueryService;
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

    private final BuyerCommandService buyerCommandService;
    private final BuyerQueryService buyerQueryService;

    @PostMapping
    public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerCommandService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
    }

    @GetMapping
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        return ResponseEntity.ok(buyerQueryService.getAllBuyers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyerResponse> getBuyer(@PathVariable Integer id) {
        return ResponseEntity.ok(buyerQueryService.getBuyer(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuyerResponse> updateBuyer(@PathVariable Integer id,
                                                     @Valid @RequestBody UpdateBuyerRequest request) {
        Buyer buyer = buyerCommandService.updateBuyer(id, request);
        return ResponseEntity.ok(BuyerResponse.from(buyer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Integer id) {
        buyerCommandService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
