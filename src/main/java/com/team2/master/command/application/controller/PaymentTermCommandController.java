package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePaymentTermRequest;
import com.team2.master.command.application.dto.UpdatePaymentTermRequest;
import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.command.application.service.PaymentTermCommandService;
import com.team2.master.query.controller.PaymentTermQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermCommandController {

    private final PaymentTermCommandService paymentTermCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<PaymentTerm>> create(@Valid @RequestBody CreatePaymentTermRequest request) {
        PaymentTerm paymentTerm = paymentTermCommandService.create(request);
        EntityModel<PaymentTerm> model = EntityModel.of(paymentTerm,
                linkTo(methodOn(PaymentTermQueryController.class).getById(paymentTerm.getPaymentTermId())).withSelfRel(),
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withRel("payment-terms"));
        URI location = linkTo(methodOn(PaymentTermQueryController.class).getById(paymentTerm.getPaymentTermId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PaymentTerm>> update(@PathVariable Integer id, @Valid @RequestBody UpdatePaymentTermRequest request) {
        PaymentTerm paymentTerm = paymentTermCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(paymentTerm,
                linkTo(methodOn(PaymentTermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withRel("payment-terms")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentTermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
