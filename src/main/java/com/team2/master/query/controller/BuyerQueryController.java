package com.team2.master.query.controller;

import com.team2.master.dto.BuyerResponse;
import com.team2.master.query.service.BuyerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerQueryController {

    private final BuyerQueryService buyerQueryService;

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
}
