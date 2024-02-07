package com.Library.services;

import com.Library.repositores.ItemRepository;
import com.Library.services.entities.Item;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementation of the InventoryService interface providing operations related to inventory management.
 */
@Service
public class InventoryServiceImpl implements InventoryService {
    private static final String NOT_AVAILABLE = "The %s item is not available in the library.";
    private static final Logger logger = Logger.getLogger(InventoryServiceImpl.class.getCanonicalName());

    private final ItemRepository itemRepository;
    @Autowired
    public InventoryServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Checks if the specified item is available in the inventory in the required quantity.
     *
     * @param item The item to check availability for.
     * @return true if the item is available in the required quantity, false otherwise.
     */
    @Override
    public boolean isAvailable(@Nonnull final Map.Entry<Item, Integer> item) {
        Optional<Item> existingItemOpt = itemRepository.findById(item.getKey().getId());
        if (existingItemOpt.isEmpty()) {
            logger.info(String.format(NOT_AVAILABLE, item.getKey()));
            return false;
        }
        Item existingItem = existingItemOpt.get();
        Integer requiredCount = item.getValue();
        return existingItem.getQuantity() >= requiredCount;
    }

    /**
     * Updates the inventory after an item is sold.
     *
     * @param sellItem The item to be sold along with the quantity.
     */
    @Override
    public void updateInventory(@Nonnull final Map.Entry<Item, Integer> sellItem) {
        Optional<Item> existingItemOpt = itemRepository.findById(sellItem.getKey().getId());

        if (existingItemOpt.isEmpty()) {
            logger.info(String.format(NOT_AVAILABLE, sellItem.getKey()));
            return;
        }
        Item existingItem = existingItemOpt.get();
        Integer requiredCount = sellItem.getValue();
        if (existingItem.getQuantity() - requiredCount == 0) {
            itemRepository.delete(existingItem);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() - requiredCount);
            itemRepository.save(existingItem);
        }
    }

    /**
     * Retrieves an item from the inventory by its ID.
     *
     * @param id The ID of the item to retrieve.
     * @return The item with the specified ID, or null if not found.
     */
    @Override
    @Nullable
    public Item getItemById(@Nonnull final Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem.orElse(null);
    }

    /**
     * Retrieves all items in the inventory.
     *
     * @return A list of all items in the inventory.
     */
    @Override
    @Nonnull
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}