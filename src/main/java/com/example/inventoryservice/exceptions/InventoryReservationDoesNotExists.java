package com.example.inventoryservice.exceptions;

public class InventoryReservationDoesNotExists extends RuntimeException {
    public InventoryReservationDoesNotExists(String message) {
        super(message);
    }
}
