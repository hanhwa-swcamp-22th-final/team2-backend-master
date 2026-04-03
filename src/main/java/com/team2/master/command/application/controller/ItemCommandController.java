package com.team2.master.command.application.controller;

import com.team2.master.command.application.dto.ChangeStatusRequest;
import com.team2.master.command.application.dto.CreateItemRequest;
import com.team2.master.command.application.dto.UpdateItemRequest;
import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import com.team2.master.command.application.service.ItemCommandService;
import com.team2.master.query.controller.ItemQueryController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemCommandController {

    private final ItemCommandService itemCommandService;

    @PostMapping
    public ResponseEntity<EntityModel<Item>> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemCommandService.createItem(request);
        EntityModel<Item> model = EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(item.getItemId())).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items"));
        URI location = linkTo(methodOn(ItemQueryController.class).getItem(item.getItemId())).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> updateItem(@PathVariable Integer id,
                                           @Valid @RequestBody UpdateItemRequest request) {
        Item item = itemCommandService.updateItem(id, request);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items")));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EntityModel<Item>> changeStatus(@PathVariable Integer id,
                                             @Valid @RequestBody ChangeStatusRequest request) {
        ItemStatus status = ItemStatus.valueOf(request.getStatus());
        Item item = itemCommandService.changeStatus(id, status);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel()));
    }
}
