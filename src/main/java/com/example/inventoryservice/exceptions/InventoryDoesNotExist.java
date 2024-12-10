package com.example.inventoryservice.exceptions;

public class InventoryDoesNotExist extends RuntimeException {
    public InventoryDoesNotExist(String message) {
        super(message);
    }
}
