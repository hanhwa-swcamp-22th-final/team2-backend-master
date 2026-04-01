package com.team2.master.query.controller;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.query.service.ItemQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemQueryController {

    private final ItemQueryService itemQueryService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemQueryService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(itemQueryService.getItem(id));
    }
}
