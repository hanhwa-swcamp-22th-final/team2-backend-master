package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateCurrencyRequest;
import com.team2.master.command.application.dto.UpdateCurrencyRequest;
import com.team2.master.command.domain.entity.Currency;
import com.team2.master.command.application.service.CurrencyCommandService;
import com.team2.master.query.controller.CurrencyQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyCommandController {

    private final CurrencyCommandService currencyCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<Currency>> create(@Valid @RequestBody CreateCurrencyRequest request) {
        Currency currency = currencyCommandService.create(request);
        EntityModel<Currency> model = EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(currency.getCurrencyId())).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies"));
        URI location = linkTo(methodOn(CurrencyQueryController.class).getById(currency.getCurrencyId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Currency>> update(@PathVariable Integer id, @Valid @RequestBody UpdateCurrencyRequest request) {
        Currency currency = currencyCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(currency,
                linkTo(methodOn(CurrencyQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CurrencyQueryController.class).getAll()).withRel("currencies")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        currencyCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
