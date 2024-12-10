package com.example.inventoryservice.services;

import com.example.inventoryservice.dtos.RevokeReservationDto;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryReservation;

public interface IInventoryReservatonService {
    InventoryReservation reserveInventoryItem(InventoryReservation inventoryReservation);
    InventoryReservation revokeReservation(RevokeReservationDto revokeReservationDto);
}
