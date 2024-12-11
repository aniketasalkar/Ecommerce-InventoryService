package com.example.inventoryservice.services;

import com.example.inventoryservice.models.InventoryTransaction;

public interface IInventoryTransactionService {
    void logTransaction(InventoryTransaction inventoryTransaction);
}
