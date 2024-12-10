package com.example.inventoryservice.exceptions;

public class InventoryReservationAlreadyExists extends RuntimeException {
    public InventoryReservationAlreadyExists(String message) {
        super(message);
    }
}
