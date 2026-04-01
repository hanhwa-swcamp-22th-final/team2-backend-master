package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePaymentTermRequest;
import com.team2.master.command.application.dto.UpdatePaymentTermRequest;
import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.command.application.service.PaymentTermCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermCommandController {

    private final PaymentTermCommandService paymentTermCommandService;

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
