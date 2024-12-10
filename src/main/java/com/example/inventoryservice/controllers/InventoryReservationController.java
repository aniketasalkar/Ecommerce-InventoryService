package com.example.inventoryservice.controllers;

import com.example.inventoryservice.dtos.InventoryItemResponseDto;
import com.example.inventoryservice.dtos.InventoryReservationRequestDto;
import com.example.inventoryservice.dtos.InventoryReservationResponseDto;
import com.example.inventoryservice.dtos.RevokeReservationDto;
import com.example.inventoryservice.models.InventoryItem;
import com.example.inventoryservice.models.InventoryReservation;
import com.example.inventoryservice.services.IInventoryReservatonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory/reservation")
public class InventoryReservationController {

    @Autowired
    IInventoryReservatonService inventoryReservatonService;

    @PostMapping("/reserve")
    public ResponseEntity<InventoryReservationResponseDto> reserveInventoryItem(@RequestBody @Valid InventoryReservationRequestDto inventoryReservationRequestDto) {

        try {
            InventoryReservation inventoryReservation = inventoryReservatonService.reserveInventoryItem(from(inventoryReservationRequestDto));

            return new ResponseEntity<>(from(inventoryReservation), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/revoke")
    public ResponseEntity<InventoryReservationResponseDto> revokeReservation(@RequestBody @Valid RevokeReservationDto revokeReservationDto) {
        try {
            InventoryReservation inventoryReservation = inventoryReservatonService.revokeReservation(revokeReservationDto);

            return new ResponseEntity<>(from(inventoryReservation), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    private InventoryReservation from(InventoryReservationRequestDto inventoryReservationRequestDto) {
        InventoryReservation inventoryReservation = new InventoryReservation();

        inventoryReservation.setOrderId(inventoryReservationRequestDto.getOrderId());
        inventoryReservation.setProductId(inventoryReservationRequestDto.getProductId());
        inventoryReservation.setQuantity(inventoryReservationRequestDto.getQuantity());

        return inventoryReservation;
    }
    private InventoryReservationResponseDto from(InventoryReservation inventoryReservation) {
        InventoryReservationResponseDto inventoryReservationResponseDto = new InventoryReservationResponseDto();

        inventoryReservationResponseDto.setInventoryItemResponse(from(inventoryReservation.getInventoryItem()));
        inventoryReservationResponseDto.setReservationId(inventoryReservation.getReservationId());
        inventoryReservationResponseDto.setReservationDate(inventoryReservation.getReservationDate());
        inventoryReservationResponseDto.setExpirationDate(inventoryReservation.getExpirationDate());
        inventoryReservationResponseDto.setInventoryReservationStatus(inventoryReservation.getInventoryReservationStatus());
        inventoryReservationResponseDto.setOrderId(inventoryReservation.getOrderId());
        inventoryReservationResponseDto.setProductId(inventoryReservation.getProductId());
        inventoryReservationResponseDto.setQuantity(inventoryReservation.getQuantity());

        return inventoryReservationResponseDto;
    }

    private InventoryItemResponseDto from(InventoryItem inventoryItem) {
        InventoryItemResponseDto inventoryItemResponseDto = new InventoryItemResponseDto();

        inventoryItemResponseDto.setProductId(inventoryItem.getProductId());
        inventoryItemResponseDto.setProductName(inventoryItem.getProductName());
        inventoryItemResponseDto.setQuantity(inventoryItem.getQuantity());
        inventoryItemResponseDto.setReservedQuantity(inventoryItem.getReservedQuantity());
        inventoryItemResponseDto.setAvailableQuantity(inventoryItem.getAvailableQuantity());

        return inventoryItemResponseDto;
    }
}
