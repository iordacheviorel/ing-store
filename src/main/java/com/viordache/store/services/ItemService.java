package com.viordache.store.services;

import com.viordache.store.dtos.ItemDTO;
import com.viordache.store.entities.Item;
import com.viordache.store.exceptions.ItemNotFoundException;
import com.viordache.store.repositories.ItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {

        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);

        return items;
    }

    public Item create(ItemDTO itemDTO) {

        var item = new Item();
        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setQuantity(itemDTO.quantity());
        item.setPrice(itemDTO.price());
        item.setSku(itemDTO.sku());

        try {
            return itemRepository.save(item);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException(e.getMessage());
        }
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItemBySku(String sku) {

        if (itemRepository.findBySku(sku).isEmpty()) {
            throw new ItemNotFoundException("Item with SKU " + sku + " not found.");
        }

        itemRepository.deleteItemBySku(sku);
    }

    public Optional<Item> findItemBySku(String sku) {
        return itemRepository.findBySku(sku);
    }
}
