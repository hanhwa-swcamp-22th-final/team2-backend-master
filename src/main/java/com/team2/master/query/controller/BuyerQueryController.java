package com.team2.master.query.controller;

import com.team2.master.query.dto.BuyerResponse;
import com.team2.master.query.service.BuyerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BuyerQueryController {

    private final BuyerQueryService buyerQueryService;

    @GetMapping("/api/buyers")
    public ResponseEntity<List<BuyerResponse>> getAllBuyers() {
        return ResponseEntity.ok(buyerQueryService.getAllBuyers());
    }

    @GetMapping("/api/buyers/{id}")
    public ResponseEntity<BuyerResponse> getBuyer(@PathVariable Integer id) {
        return ResponseEntity.ok(buyerQueryService.getBuyer(id));
    }

    @GetMapping("/api/buyers/client/{clientId}")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }

    @GetMapping("/api/clients/{clientId}/buyers")
    public ResponseEntity<List<BuyerResponse>> getBuyersByClientNested(@PathVariable Integer clientId) {
        return ResponseEntity.ok(buyerQueryService.getBuyersByClientId(clientId));
    }
}
