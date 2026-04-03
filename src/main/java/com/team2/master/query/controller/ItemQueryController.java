package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ItemListResponse;
import com.team2.master.query.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @GetMapping
    public ResponseEntity<PagedResponse<ItemListResponse>> getItems(
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String itemCategory,
            @RequestParam(required = false) String itemStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(itemQueryService.getItems(itemName, itemCategory, itemStatus, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> getItem(@PathVariable Integer id) {
        Item item = itemQueryService.getItem(id);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(ItemQueryController.class).getItem(id)).withSelfRel(),
                linkTo(methodOn(ItemQueryController.class).getItems(null, null, null, 0, 10)).withRel("items")));
    }
}
