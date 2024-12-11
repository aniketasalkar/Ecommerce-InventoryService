package com.example.inventoryservice.services;

import com.example.inventoryservice.models.InventoryTransaction;
import com.example.inventoryservice.models.TransactionType;
import com.example.inventoryservice.repositories.InventoryTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InventoryTransactionService implements IInventoryTransactionService {

    @Autowired
    InventoryTransactionRepository inventoryTransactionRepository;

    @Override
    public void logTransaction(InventoryTransaction inventoryTransaction) {

        Date now = new Date();
        inventoryTransaction.setCreatedAt(now);
        inventoryTransaction.setUpdatedAt(now);

        inventoryTransactionRepository.save(inventoryTransaction);
    }
}
