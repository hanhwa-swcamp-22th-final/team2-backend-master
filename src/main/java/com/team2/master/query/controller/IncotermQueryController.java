package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Incoterm;
import com.team2.master.query.service.IncotermQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/incoterms")
@RequiredArgsConstructor
public class IncotermQueryController {

    private final IncotermQueryService incotermQueryService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Incoterm>>> getAll() {
        List<EntityModel<Incoterm>> models = incotermQueryService.getAll().stream()
                .map(i -> EntityModel.of(i,
                        linkTo(methodOn(IncotermQueryController.class).getById(i.getIncotermId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(IncotermQueryController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Incoterm>> getById(@PathVariable Integer id) {
        Incoterm incoterm = incotermQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(incoterm,
                linkTo(methodOn(IncotermQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(IncotermQueryController.class).getAll()).withRel("incoterms")));
    }
}
