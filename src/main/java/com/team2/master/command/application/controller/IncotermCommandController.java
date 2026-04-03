package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreateIncotermRequest;
import com.team2.master.command.application.dto.UpdateIncotermRequest;
import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.command.application.service.IncotermCommandService;
import com.team2.master.query.controller.IncotermQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
public class IncotermCommandController {

    private final IncotermCommandService incotermCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<Incoterm>> create(@Valid @RequestBody CreateIncotermRequest request) {
        Incoterm incoterm = incotermCommandService.create(request);
        EntityModel<Incoterm> model = EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(incoterm.getIncotermId())).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms"));
        URI location = linkTo(methodOn(IncotermQueryController.class).getById(incoterm.getIncotermId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Incoterm>> update(@PathVariable Integer id, @Valid @RequestBody UpdateIncotermRequest request) {
        Incoterm incoterm = incotermCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        incotermCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
