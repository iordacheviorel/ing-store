package com.viordache.store.repositories;

import com.viordache.store.entities.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Optional<Item> findBySku(String sku);
    void deleteItemBySku(String sku);
}
