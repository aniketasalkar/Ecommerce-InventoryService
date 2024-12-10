package com.example.inventoryservice.services;

import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryReservation;

import java.util.Optional;

public interface IInventoryService {
    InventoryItem createInventoryItem(InventoryItem inventoryItem);
    InventoryItem getInventoryItem(Long productId);
    InventoryItem updateInventory(Long productId, InventoryItem inventoryItem);
}
