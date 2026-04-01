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
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerCommandController {

    private final BuyerCommandService buyerCommandService;

    @PostMapping
    public ResponseEntity<BuyerResponse> createBuyer(@Valid @RequestBody CreateBuyerRequest request) {
        Buyer buyer = buyerCommandService.createBuyer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BuyerResponse.from(buyer));
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
