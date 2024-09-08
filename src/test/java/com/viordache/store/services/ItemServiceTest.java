package com.viordache.store.services;

import com.viordache.store.dtos.ItemDTO;
import com.viordache.store.entities.Item;
import com.viordache.store.exceptions.ItemNotFoundException;
import com.viordache.store.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnListOfItems() {
        // setup
        Item item1 = new Item();
        Item item2 = new Item();
        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        // execute
        List<Item> result = itemService.findAll();

        // verify
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void create_shouldSaveAndReturnNewItem() {
        // setup
        ItemDTO itemDTO = new ItemDTO("Item 1", "Description 1", 10, new BigDecimal("9.99"), "SKU123");
        Item item = new Item();
        item.setName(itemDTO.name());
        item.setDescription(itemDTO.description());
        item.setQuantity(itemDTO.quantity());
        item.setPrice(itemDTO.price());
        item.setSku(itemDTO.sku());

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // execute
        Item result = itemService.create(itemDTO);

        // verify
        assertNotNull(result);
        assertEquals("Item 1", result.getName());
        assertEquals("Description 1", result.getDescription());
        assertEquals(10, result.getQuantity());
        assertEquals(new BigDecimal("9.99"), result.getPrice());
        assertEquals("SKU123", result.getSku());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void create_whenDuplicateKey_shouldThrowException() {
        // setup
        ItemDTO itemDTO = new ItemDTO("Item 1", "Description 1", 10, new BigDecimal("9.99"), "SKU123");
        when(itemRepository.save(any(Item.class))).thenThrow(DataIntegrityViolationException.class);

        // execute & verify
        assertThrows(DuplicateKeyException.class, () -> itemService.create(itemDTO));
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void save_shouldSaveAndReturnItem() {
        // setup
        Item item = new Item();
        item.setSku("SKU123");
        when(itemRepository.save(item)).thenReturn(item);

        // execute
        Item result = itemService.save(item);

        // verify
        assertNotNull(result);
        assertEquals("SKU123", result.getSku());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void deleteItemBySku_whenItemExists_shouldDeleteItem() {
        // setup
        String sku = "SKU123";
        when(itemRepository.findBySku(sku)).thenReturn(Optional.of(new Item()));

        // execute
        itemService.deleteItemBySku(sku);

        // verify
        verify(itemRepository, times(1)).findBySku(sku);
        verify(itemRepository, times(1)).deleteItemBySku(sku);
    }

    @Test
    void deleteItemBySku_whenItemDoesNotExist_shouldThrowException() {
        // setup
        String sku = "SKU123";
        when(itemRepository.findBySku(sku)).thenReturn(Optional.empty());

        // execute & verify
        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItemBySku(sku));
        verify(itemRepository, times(1)).findBySku(sku);
        verify(itemRepository, never()).deleteItemBySku(sku);
    }

    @Test
    void findItemBySku_shouldReturnItemIfExists() {
        // setup
        String sku = "SKU123";
        Item item = new Item();
        item.setSku(sku);
        when(itemRepository.findBySku(sku)).thenReturn(Optional.of(item));

        // execute
        Optional<Item> result = itemService.findItemBySku(sku);

        // verify
        assertTrue(result.isPresent());
        assertEquals(sku, result.get().getSku());
        verify(itemRepository, times(1)).findBySku(sku);
    }

    @Test
    void findItemBySku_shouldReturnEmptyIfNotExists() {
        // setup
        String sku = "SKU123";
        when(itemRepository.findBySku(sku)).thenReturn(Optional.empty());

        // execute
        Optional<Item> result = itemService.findItemBySku(sku);

        // verify
        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).findBySku(sku);
    }
}