package com.team2.master.query.controller;

import com.team2.master.query.dto.PortResponse;
import com.team2.master.query.service.PortQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortQueryController {

    private final PortQueryService portQueryService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PortResponse>>> getAll() {
        List<EntityModel<PortResponse>> models = portQueryService.getAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PortQueryController.class).getById(p.getId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PortQueryController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PortResponse>> getById(@PathVariable Integer id) {
        PortResponse port = portQueryService.getById(id);
        return ResponseEntity.ok(EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports")));
    }
}
