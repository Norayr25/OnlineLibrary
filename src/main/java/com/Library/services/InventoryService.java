package com.Library.services;

import com.Library.services.entities.Item;

import java.util.List;
import java.util.Map;

/**
 * Interface defining operations related to inventory management.
 */
public interface InventoryService {
    boolean isAvailable(Map.Entry<Item, Integer> entry);
    void updateInventory(Map.Entry<Item, Integer> entry);
    Item getItemById(Long id);
    List<Item> getAllItems();
}
