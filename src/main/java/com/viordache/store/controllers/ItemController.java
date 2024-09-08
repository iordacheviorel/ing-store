package com.viordache.store.controllers;

import com.viordache.store.dtos.ItemDTO;
import com.viordache.store.entities.Item;
import com.viordache.store.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/items")
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Item> createItem(@RequestBody ItemDTO itemDTO) {

        Item item = itemService.create(itemDTO);

        return ResponseEntity.ok(item);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @DeleteMapping("/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteItem(@PathVariable String sku) {

        itemService.deleteItemBySku(sku);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{sku}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Item> updatePrice(@PathVariable String sku, @RequestBody Map<String, Object> updates) {
        Optional<Item> optionalItem = itemService.findItemBySku(sku);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            if (updates.containsKey("price")) {
                item.setPrice(new BigDecimal(updates.get("price").toString()));
            }
            itemService.save(item);
            return ResponseEntity.ok(item);
        }

        return ResponseEntity.notFound().build();
    }
}
