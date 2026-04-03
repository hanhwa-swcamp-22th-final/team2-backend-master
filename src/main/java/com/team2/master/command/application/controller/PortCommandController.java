package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.CreatePortRequest;
import com.team2.master.query.dto.PortResponse;
import com.team2.master.command.application.dto.UpdatePortRequest;
import com.team2.master.command.application.service.PortCommandService;
import com.team2.master.query.controller.PortQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/ports")
@RequiredArgsConstructor
public class PortCommandController {

    private final PortCommandService portCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<PortResponse>> create(@Valid @RequestBody CreatePortRequest request) {
        PortResponse port = portCommandService.create(request);
        EntityModel<PortResponse> model = EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(port.getId())).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports"));
        URI location = linkTo(methodOn(PortQueryController.class).getById(port.getId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PortResponse>> update(@PathVariable Integer id, @Valid @RequestBody UpdatePortRequest request) {
        PortResponse port = portCommandService.update(id, request);
        return ResponseEntity.ok(EntityModel.of(port,
                linkTo(methodOn(PortQueryController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PortQueryController.class).getAll()).withRel("ports")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        portCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
