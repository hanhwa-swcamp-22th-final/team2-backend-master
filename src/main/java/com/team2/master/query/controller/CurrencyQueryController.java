package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Currency;
import com.team2.master.query.service.CurrencyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyQueryController {

    private final CurrencyQueryService currencyQueryService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Currency>>> getAll() {
        List<EntityModel<Currency>> models = currencyQueryService.getAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CurrencyQueryController.class).getById(c.getCurrencyId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Currency>> getById(@PathVariable Integer id) {
        Currency currency = currencyQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies")));
    }
}
