package com.team2.master.command.controller;

import com.team2.master.dto.ChangeStatusRequest;
import com.team2.master.dto.CreateItemRequest;
import com.team2.master.dto.UpdateItemRequest;
import com.team2.master.entity.Item;
import com.team2.master.entity.enums.ItemStatus;
import com.team2.master.command.service.ItemCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemCommandController {

    private final ItemCommandService itemCommandService;

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody CreateItemRequest request) {
        Item item = itemCommandService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer id,
                                           @Valid @RequestBody UpdateItemRequest request) {
        return ResponseEntity.ok(itemCommandService.updateItem(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Item> changeStatus(@PathVariable Integer id,
                                             @Valid @RequestBody ChangeStatusRequest request) {
        ItemStatus status = ItemStatus.valueOf(request.getStatus());
        return ResponseEntity.ok(itemCommandService.changeStatus(id, status));
    }
}
