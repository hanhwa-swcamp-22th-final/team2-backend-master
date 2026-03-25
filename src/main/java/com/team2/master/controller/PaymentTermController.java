package com.team2.master.controller;

import com.team2.master.dto.CreatePaymentTermRequest;
import com.team2.master.dto.UpdatePaymentTermRequest;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.service.PaymentTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermController {

    private final PaymentTermService paymentTermService;

    @GetMapping
    public ResponseEntity<List<PaymentTerm>> getAll() {
        return ResponseEntity.ok(paymentTermService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTerm> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentTermService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentTerm> create(@RequestBody CreatePaymentTermRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentTermService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTerm> update(@PathVariable Integer id, @RequestBody UpdatePaymentTermRequest request) {
        return ResponseEntity.ok(paymentTermService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentTermService.delete(id);
        return ResponseEntity.ok().build();
    }
}
