package com.team2.master.controller;

import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.CreateItemRequest;
import com.team2.master.dto.UpdateItemRequest;
import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id,
                                           @Valid @RequestBody UpdateItemRequest request) {
        return ResponseEntity.ok(itemService.updateItem(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Item> changeStatus(@PathVariable Integer id,
                                             @Valid @RequestBody ChangeStatusRequest request) {
        ItemStatus status = ItemStatus.valueOf(request.getStatus());
        return ResponseEntity.ok(itemService.changeStatus(id, status));
    }
}
