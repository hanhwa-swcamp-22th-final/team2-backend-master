package com.team2.master.controller;

import com.team2.master.dto.CreatePaymentTermRequest;
import com.team2.master.dto.UpdatePaymentTermRequest;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.service.PaymentTermCommandService;
import com.team2.master.service.PaymentTermQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermController {

    private final PaymentTermCommandService paymentTermCommandService;
    private final PaymentTermQueryService paymentTermQueryService;

    @GetMapping
    public ResponseEntity<List<PaymentTerm>> getAll() {
        return ResponseEntity.ok(paymentTermQueryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTerm> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentTermQueryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentTerm> create(@Valid @RequestBody CreatePaymentTermRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentTermCommandService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTerm> update(@PathVariable Integer id, @Valid @RequestBody UpdatePaymentTermRequest request) {
        return ResponseEntity.ok(paymentTermCommandService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentTermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
