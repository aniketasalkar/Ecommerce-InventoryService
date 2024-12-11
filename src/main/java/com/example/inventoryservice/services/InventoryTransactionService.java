package com.example.inventoryservice.services;

import com.example.inventoryservice.models.InventoryTransaction;
import com.example.inventoryservice.models.TransactionType;
import com.example.inventoryservice.repositories.InventoryTransactionRepository;
import com.example.inventoryservice.utils.IdGenerator;
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
        inventoryTransaction.setTransactionID(IdGenerator.generateId(14));

        inventoryTransactionRepository.save(inventoryTransaction);
    }
}
