package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.common.PagedResponse;
import com.team2.master.query.dto.ItemListResponse;
import com.team2.master.query.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Item> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(itemQueryService.getItem(id));
    }
}
