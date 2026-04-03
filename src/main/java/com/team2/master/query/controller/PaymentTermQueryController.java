package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.query.service.PaymentTermQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/payment-terms")
@RequiredArgsConstructor
public class PaymentTermQueryController {

    private final PaymentTermQueryService paymentTermQueryService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PaymentTerm>>> getAll() {
        List<EntityModel<PaymentTerm>> models = paymentTermQueryService.getAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PaymentTermQueryController.class).getById(p.getPaymentTermId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PaymentTerm>> getById(@PathVariable Integer id) {
        PaymentTerm paymentTerm = paymentTermQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(paymentTerm,
                linkTo(methodOn(PaymentTermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PaymentTermQueryController.class).getAll()).withRel("payment-terms")));
    }
}
