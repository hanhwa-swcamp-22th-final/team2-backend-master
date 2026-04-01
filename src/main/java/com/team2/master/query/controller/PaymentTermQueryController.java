package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.query.service.PaymentTermQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermQueryController {

    private final PaymentTermQueryService paymentTermQueryService;

    @GetMapping
    public ResponseEntity<List<PaymentTerm>> getAll() {
        return ResponseEntity.ok(paymentTermQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTerm> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentTermQueryService.getById(id));
    }
}
