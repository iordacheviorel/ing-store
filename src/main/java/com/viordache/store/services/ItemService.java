package com.viordache.store.services;

import com.viordache.store.dtos.ItemDTO;
import com.viordache.store.entities.Item;
import com.viordache.store.exceptions.ItemNotFoundException;
import com.viordache.store.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {

        LOGGER.info("Finding all items");
        List<Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);

        return items;
    }

    public Item create(ItemDTO itemDTO) {

        LOGGER.info("Creating new item with SKU: {}", itemDTO.sku());
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
        LOGGER.info("Saving item with SKU: {}", item.getSku());
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItemBySku(String sku) {

        LOGGER.info("Deleting item with SKU: {}", sku);
        if (itemRepository.findBySku(sku).isEmpty()) {
            LOGGER.error("Couldn't find item with SKU {}", sku);
            throw new ItemNotFoundException("Item with SKU " + sku + " not found.");
        }

        itemRepository.deleteItemBySku(sku);
    }

    public Optional<Item> findItemBySku(String sku) {

        LOGGER.info("Finding item with SKU: {}", sku);
        return itemRepository.findBySku(sku);
    }
}
